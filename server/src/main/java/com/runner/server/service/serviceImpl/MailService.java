package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.MailReportSummary;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.dao.entity.po.Mail;
import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.mapper.EnvMachineMapper;
import com.runner.server.dao.mapper.MailMapper;
import com.runner.server.service.utils.CacheUtil;
import com.runner.server.service.utils.ChartsUtil;
import com.runner.server.service.utils.DateUtil;
import com.runner.server.service.utils.LogUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class MailService {

    @Resource
    public MailMapper mailMapper;

    private MsgResponseObject msgResponseObject=new MsgResponseObject() ;


    @Value("${pic_path}")
    private String picPath;  //饼图临时地址

    private String picId = "abcdefg1234567890";

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private BigDecimal amount_0 = new BigDecimal(0);
    private BigDecimal amount_100 = new BigDecimal(100);


 
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:12
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        Mail mail=new Mail();
        BeanUtils.populate(mail, pageObject.getData());
        int total=0;
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<Mail> list=mailMapper.queryPageData(offset,pageObject.getLimit(),mail);
        total=mailMapper.queryCountByExample(mail);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }


   /**
    * @description 
    * @author 星空梦语
    * @date 2021/3/9 下午4:12
    */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        Mail mail=new Mail();
        BeanUtils.populate(mail, pageObject.getData());
        msgResponseObject.setCode(201);
        if(StringUtils.isBlank(mail.getProjectCode()) ||StringUtils.isBlank(mail.getMailTitle())
                ||StringUtils.isBlank(mail.getMailReceive())){
            msgResponseObject.setMsg("邮件配置不能为空，请确认");
            return  msgResponseObject;
        }

        Mail p=new Mail();
        p.setProjectCode(mail.getProjectCode());
        List<Mail> plist= mailMapper.queryPageData(0,10,p);
        mail.setProjectName(null);
        if(mail.getId()!=null){
            for(Mail data:plist){
                if(!data.getId().equals(mail.getId())){
                    msgResponseObject.setMsg("项目配置重复");
                    return  msgResponseObject;
                }
            }
            Example example=new Example(Mail.class);
            example.createCriteria().andEqualTo("id",mail.getId());
            msgResponseObject.setMsg("修改失败");
            if(mailMapper.updateByExampleSelective(mail,example)>0){
                msgResponseObject.setMsg("修改成功");
                msgResponseObject.setCode(200);
            }
        }else{
            if(plist.size()>=1){
                msgResponseObject.setMsg("项目配置重复");
                return  msgResponseObject;
            }
            mail.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(mailMapper.insertSelective(mail)>0){
                msgResponseObject.setMsg("新增成功");
                msgResponseObject.setCode(200);
            }else{
                msgResponseObject.setMsg("新增失败");
            }
        }
        return  msgResponseObject;
    }

    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:13
     */
    public MsgResponseObject delete(int id) throws Exception {
        Example example=new Example(Mail.class);
        example.createCriteria().andEqualTo("id",id);
        msgResponseObject.setMsg("删除失败");
        if(mailMapper.deleteByExample(example)>0){
            msgResponseObject.setMsg("删除成功");
            msgResponseObject.setCode(200);
        }
        return msgResponseObject;
    }



    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:13
     */
    public MsgResponseObject sendMailReport(MailReportSummary mailReportSummary) throws Exception {
        LogUtil.info("测试任务执行完成，开始发送邮件报告...");
        msgResponseObject.setCode(200);
        Mail  mail=new Mail();
        mail.setProjectName(mailReportSummary.getSys());
        mail.setStatus("0");
        String[] receiver =null;
        List<Mail>mailList=mailMapper.queryPageData(0,1,mail);
        if(mailList.size()==0){
            msgResponseObject.setCode(201);
            msgResponseObject.setMsg(" 测试报告邮件发送失败，执行项目收件人配置为空");
            return  msgResponseObject;
        }else{
            receiver=mailList.get(0).getMailReceive().split(",");
        }
        String subject = mailList.get(0).getMailTitle();
        //生成饼图
        genJFreePic(mailReportSummary);
        //获取转换后的模板内容
        String content = getReportTemplate(mailReportSummary, subject);

        //大饼图邮件参数
        Map picParam = new HashMap<>();
        picParam.put("picId", picId);
        picParam.put("picPath", picPath);
        sendAutoReportMail(content, receiver, subject, picParam);
        return   msgResponseObject;
    }


    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:13
     */
    public void genJFreePic(MailReportSummary mailReportSummary) throws Exception {
        //生成大饼图
        DefaultPieDataset dfp = new DefaultPieDataset();
        dfp.setValue("成功数", mailReportSummary.getSuccess());
        dfp.setValue("失败数", mailReportSummary.getFailed());
        dfp.setValue("异常数", mailReportSummary.getError());
        ChartsUtil.get2DPieChart("图例汇总", dfp, picPath);
    }


    /***
     * @Author: wanglei
     * @Description: 获取转换后的摸吧内容
     * @Date: Created in  2020/02/05
     */
    public String getReportTemplate(MailReportSummary mailReportSummary, String subject) {
        Context context = new Context();
        context.setVariable("start_time", mailReportSummary.getStartDate());
        context.setVariable("end_time", mailReportSummary.getEndDate());
        context.setVariable("used_time", DateUtil.dateDifference(mailReportSummary.getStartDate(), mailReportSummary.getEndDate()) + "秒");
        context.setVariable("sum_all_cases", mailReportSummary.getTotal());
        context.setVariable("sum_success_cases", mailReportSummary.getSuccess());
        BigDecimal rate = amount_0;
        if (mailReportSummary.getTotal() > 0) {
            rate = new BigDecimal(mailReportSummary.getSuccess()).divide(new BigDecimal(mailReportSummary.getTotal()), 4, RoundingMode.HALF_UP).
                    multiply(amount_100).setScale(2, RoundingMode.HALF_UP);
        }
        context.setVariable("sum_success_rate", rate + "%");
        context.setVariable("sum_failed_cases", mailReportSummary.getFailed());
        context.setVariable("dataList", mailReportSummary.getData());
        context.setVariable("subject", subject);
        context.setVariable("imgPathId", picId);
        return this.templateEngine.process("/autoReportTemplet", context);

    }


    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:15
     */
    public void sendAutoReportMail(String content, String[] receiver, String subject, Map picParam, MultipartFile[]... uploadFiles) throws Exception {
        System.setProperty("mail.mime.splitlongparameters", "false");  //防止附件名称长被截取
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(content, true);
        FileSystemResource file = new FileSystemResource(new File(picParam.get("picPath").toString()));
        helper.addInline(picParam.get("picId").toString(), file);
        mailSender.send(mimeMessage); //发送邮件
    }



}
