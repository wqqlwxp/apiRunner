package com.runner.server.service.serviceImpl;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSONArray;
import com.runner.server.dao.entity.po.*;
import com.runner.server.dao.mapper.ZkDataMapper;
import com.runner.server.dao.mapper.ZkMapper;
import com.runner.server.service.utils.*;
import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.*;
import com.runner.server.dao.mapper.ReportMapper;
import com.runner.server.dao.mapper.CaseMapper;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CaseService {

    @Resource
    private CaseMapper caseMapper;

    @Resource
    private ReportMapper caseLogMapper;

    @Resource
    private ZkMapper zkMapper;

    @Resource
    private ZkDataMapper zkDataMapper;

    @Resource
    private EnvMachineService envMachineService;

    @Resource
    private ReportService reportService;

    @Resource
    private CipherService cipherService;






    /**
     * @description  保存用例
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public MsgResponseObject editCase(PageObject pageObject) throws Exception {
        TestCase testCase=new TestCase();
        MsgResponseObject msgResponseObject=new MsgResponseObject();
        msgResponseObject.setCode(201);
        BeanUtils.populate(testCase, pageObject.getData());
        if(StringUtils.isBlank(testCase.getModuleCode()) ||StringUtils.isBlank(testCase.getProjectCode()) ){
            msgResponseObject.setMsg("编码或名称不能为空，请确认");
            return  msgResponseObject;
        }
        testCase.setModuleName(null);
        testCase.setProjectName(null);
        List<String> ids=new ArrayList<>();
        if(testCase.getCaseData()!=null){
            List<ReqCaseData> reqInterfaceDatas= JSONArray.parseArray(testCase.getCaseData(), ReqCaseData.class);
            reqInterfaceDatas.forEach(item->{
                ids.add(item.getReqInterface().getId());
            });
            testCase.setInterfaceIds(JSON.toJSONString(ids));
        }
        if(testCase.getId()!=0 ){
            Example example=new Example(TestCase.class);
            example.createCriteria().andEqualTo("id",testCase.getId());
            if(caseMapper.updateByExampleSelective(testCase,example)>0){
                updateTestCaseCache();
                msgResponseObject.setMsg("修改成功");
                msgResponseObject.setCode(200);
            }else{
                msgResponseObject.setMsg("修改失败");
            }
        }else{
            testCase.setCaseData("");
            testCase.setPreData("");
            testCase.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if( caseMapper.insertSelective(testCase)>0){
                updateTestCaseCache();
                msgResponseObject.setMsg("新增成功");
                msgResponseObject.setCode(200);
            }else{
                msgResponseObject.setMsg("新增失败");
            }
        }
        return msgResponseObject;

    }





    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:11
     */
    public String query(PageObject pageObject) throws Exception {
        TestCase testCase=new TestCase();
        BeanUtils.populate(testCase, pageObject.getData());
        int total=0;
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<TestCase> list=caseMapper.queryPageData(offset,pageObject.getLimit(),testCase,null);
        total=caseMapper.queryCountByExample(testCase);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }



    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:11
     */
    public String queryDetail(String  id) {
        TestCase data=caseMapper.querySingleCase(id);
        return JSON.toJSONString(data);
    }








    /**
     * @description 删除数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject delete(int id){
        MsgResponseObject msgResponseObject=new MsgResponseObject();
        Example example=new Example(TestCase.class);
        example.createCriteria().andEqualTo("id",id);
        msgResponseObject.setMsg("删除失败");
        msgResponseObject.setCode(201);
        if(caseMapper.deleteByExample(example)>0){
            msgResponseObject.setMsg("删除成功");
            msgResponseObject.setCode(200);
        }
        return msgResponseObject;
    }



    /**
     * @description 导入数据
     * @author 星空梦语
     * @date 2021/3/7 下午6:41
     */
    public MsgResponseObject importCase(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        MsgResponseObject msgResponseObject=new MsgResponseObject();
        TestCase testCase=new TestCase();
        BeanUtils.populate(testCase, pageObject.getData());
        int total= caseMapper.queryCountByExample(testCase);
        if(total==0){
            msgResponseObject.setCode(201);
            msgResponseObject.setMsg("同项目和模块下，无此用例id数据");
        }else{
            testCase=caseMapper.querySingleCase(String.valueOf(testCase.getId()));
            msgResponseObject.setObject(testCase.getCaseData());
            msgResponseObject.setCode(200);
            msgResponseObject.setMsg("导入成功");
        }
        return  msgResponseObject;
    }





    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/9/16 18:07
     */
    public List<TestCase> queryByCode(String projectCode, String moduleCode){
        TestCase testCase=new TestCase();
        testCase.setProjectCode(projectCode);
        testCase.setModuleCode(moduleCode);
        return caseMapper.queryPageData(0,1000,testCase,null);
    }



    /**
     * @description  dubbo测试用例执行
     * @author 星空梦语
     * @date 2021/3/24 下午6:20
     */
    public Map invokeMethod(ReqRequest reqRequest) {
        String zkAddress="";
        if(reqRequest.getPlanID()!=null){
            List<EnvMachine> envMachineList= envMachineService.getEnvMachineCache();
            for(EnvMachine env:envMachineList){
                if (env.getProjectCode().equals(reqRequest.getProjectCode()) && env.getEnvCode().equals(reqRequest.getEnv())){
                    zkAddress=env.getIp()+":"+env.getPort();
                    break;
                }
            }
        }
        List<ReqCaseData> reqCaseDataList=reqRequest.getReqCaseDatas();
        List<Map<String, Object>> resultList=new ArrayList<>();
        List<CaseLog>  caseLogList=new ArrayList<>();

        for (ReqCaseData reqCaseData : reqCaseDataList) {
            if(reqRequest.getPlanID()==null){
                ZkConfig zkConfig=zkMapper.queryAll().stream().filter(item->(item.getId()==reqCaseData.getZkId())).collect(Collectors.toList()).get(0);
                zkAddress=zkConfig.getZkIp();
            }

            ZkData zkData=new ZkData();
            zkData.setZkId(reqCaseData.getZkId());
            zkData.setAppName(reqCaseData.getZkApp());
            zkData.setInterfaceName(reqCaseData.getZkInterface());
            zkData.setMethodName(reqCaseData.getZkMethod());
            zkData=zkDataMapper.queryPageData(0,1,zkData).get(0);
            Map<String, Object> map=new HashMap<>();
            if(zkData!=null){
                try{
                    List<String> typeList = JSONArray.parseArray(zkData.getParamTypes(), String.class);
                    List paramList = JSONArray.parseArray(zkData.getParamDatas());
                    ReferenceConfig<GenericService> referenceConfig = ZookeeperUtil.getReference(zkData.getInterfaceName(), zkAddress,
                            zkData.getVersion(), zkData.getAppName(), zkData.getGroup());
                    GenericService genericService = referenceConfig.get();
                    String[] types =typeList.toArray(new String[typeList.size()]);
                    Object[] params =paramList.toArray(new Object[paramList.size()]);
                    Object result = genericService.$invoke(zkData.getMethodName(), types,params );
                    map.put(zkData.getMethodName(),result);
                    LogUtil.info("泛化调用结果：" + JSON.toJSONString(result));
                    if(reqRequest.getPlanID()!=null){
                        CaseLog caseLog=verifResponseData(reqRequest,reqCaseData,JSON.toJSONString(result),1,"dubbo");
                        TestCase testCase=new TestCase();
                        testCase.setId(Integer.parseInt(caseLog.getCaseId()));
                        testCase.setProjectCode(caseLog.getProjectCode());
                        testCase.setModuleCode(caseLog.getModuleCode());
                        TestCase testCase1=caseMapper.queryPageData(0,1,testCase,null).get(0);
                        caseLog.setProjectName(testCase1.getProjectName());
                        caseLog.setModuleName(testCase1.getModuleName());
                        caseLog.setCaseDesc(testCase1.getCaseDesc());
                        caseLog.setServiceType("dubbo");
                        caseLogList.add(caseLog);
                    }

                }catch (Exception e){
                    map.put(zkData.getMethodName(),"方法调用异常,请先确认与服务提供者网络访问是否正常，异常原因："+JSON.toJSONString(e.getMessage()));
                    e.printStackTrace();
                }
            }else{
                map.put(zkData.getMethodName(),"无对应方法，请确认："+zkData.getMethodName());
            }
            resultList.add(map);
        }
        Map resultMap=new HashMap();
        resultMap.put("logList",caseLogList);
        resultMap.put("result",resultList);
        return resultMap;
    }






    /**
     * @description  用例执行
     * @author 星空梦语
     * @date 2021/2/17 下午2:37
     */
    public List<CaseLog> runCase(ReqRequest reqRequest) throws Exception {
        LogUtil.info("用例执行接收数据："+JSON.toJSONString(reqRequest));
        EnvMachine envMachine=null;
        List<EnvMachine> envMachineList= envMachineService.getEnvMachineCache();
        for(EnvMachine env:envMachineList){
            if (env.getProjectCode().equals(reqRequest.getProjectCode()) && env.getEnvCode().equals(reqRequest.getEnv())){
                envMachine=env;
                break;
            }
        }
        List<CaseLog> caseLogList=new ArrayList<>();
        HttpResponseData preHttpResponseData=null;
//        执行前置
        if(reqRequest.getReqPre()!=null){
            LogUtil.info("开始执行前置用例...");
            TestCase testCase=reqRequest.getReqPre();
            testCase=caseMapper.querySingleCase(testCase.getId().toString());
            List<ReqCaseData> reqInterfaceDatas= JSONArray.parseArray(testCase.getCaseData(), ReqCaseData.class);
            CaseRunData caseRunData=runDetail(reqRequest,reqInterfaceDatas,envMachine,0,preHttpResponseData);
            preHttpResponseData=caseRunData.getHttpResponseData();
            LogUtil.info("前置用例执行结束...");
            caseLogList.addAll(caseRunData.getCaseLogList());
        }
        LogUtil.info("开始执行正常测试用例...");
        CaseRunData caseRunData=runDetail(reqRequest,reqRequest.getReqCaseDatas(),envMachine,1,preHttpResponseData);
        caseLogList.addAll(caseRunData.getCaseLogList());
        return caseLogList;
    }






    /**
     * @description   用例明细执行
     * @author 星空梦语
     * @date 2021/2/17 下午2:37
     */
    public CaseRunData  runDetail(ReqRequest reqRequest,List<ReqCaseData> reqInterfaceDatas,EnvMachine envMachine,int type,HttpResponseData preHttpResponseData) throws Exception {
        HttpResponseData httpResponseData=null;
        List<CaseLog> caseLogList=new ArrayList<>();
        String projectCode=reqRequest.getProjectCode();
        String moduleCode=reqRequest.getModuleCode();
        //如果是前置类型，则响应对象赋值给前置响应对象
        if(type==0){
            projectCode=reqRequest.getReqPre().getProjectCode();
            moduleCode=reqRequest.getReqPre().getModuleCode();
        }
        for(ReqCaseData reqInterfaceData:reqInterfaceDatas){
            reqInterfaceData.setEnvMachine(envMachine);
            replaceParamValue(httpResponseData,reqInterfaceData,preHttpResponseData);  //响应参数替换
            encryptValue(projectCode,moduleCode,reqInterfaceData);  //参数替换后再进行加密
            switch (reqInterfaceData.getReqInterface().getRequestType()){
                case "post":
                    httpResponseData=HttpClientUtil.postData(reqInterfaceData);
                    break;
                case "get":
                    httpResponseData=HttpClientUtil.getData(reqInterfaceData);
                    break;
            }
            // 在此处解密响应数据，解析出的json后续仍然使用jsonpath替换即可
            String respText=cipherService.decrypt(projectCode,moduleCode,httpResponseData.getRespText().toString());
            httpResponseData.setRespText(respText);
            CaseLog caseLog=verifResponseData(reqRequest,reqInterfaceData,httpResponseData.getRespText().toString(),type,"http/https");
            TestCase testCase=new TestCase();
            testCase.setId(Integer.parseInt(caseLog.getCaseId()));
            testCase.setProjectCode(caseLog.getProjectCode());
            testCase.setModuleCode(caseLog.getModuleCode());
            TestCase testCase1=caseMapper.queryPageData(0,1,testCase,null).get(0);
            caseLog.setProjectName(testCase1.getProjectName());
            caseLog.setModuleName(testCase1.getModuleName());
            caseLog.setCaseDesc(testCase1.getCaseDesc());
            caseLog.setServiceType("http/https");
            caseLogList.add(caseLog);
        }
        if(StringUtils.isNotBlank(reqRequest.getPlanID())){ //单独执行时才在内部更新执行记录缓存，由测试计划执行时，在外部更新
            reportService.updateReportCache();
        }
        //前置用例赋值
        if(type==0){
            preHttpResponseData=httpResponseData;
        }
        CaseRunData caseRunData=new CaseRunData();
        caseRunData.setCaseLogList(caseLogList);
        caseRunData.setHttpResponseData(preHttpResponseData);
        return caseRunData;
    }





    /**
     * @description  参数替换，针对同用例下多个接口需要串行执行或者存在前置用例时时，第二个接口开始可以使用前一个接口响应的header和body数据
     * @author 星空梦语
     * @date 2021/2/17 下午10:23
     */
    public void replaceParamValue(HttpResponseData httpResponseData,ReqCaseData reqInterfaceData,HttpResponseData preHttpResponseData){
        if(httpResponseData!=null){
            for(ReqHeader reqHeader: reqInterfaceData.getReqHeaders()){
                if(reqHeader.getValue().startsWith("@")){ //请求头从上一个接口的响应头中获取
                    String data=replaceResponseHeaders(httpResponseData.getRespHeaders(),reqHeader.getValue().substring(1));
                    if(data!=null) reqHeader.setValue(data);
                }
            }
            LogUtil.info("正常header参数变量替换后："+JSON.toJSONString(reqInterfaceData.getReqHeaders()));
            if(ConverterUtil.isJSON(httpResponseData.getRespText().toString())){
                for(ReqHeader reqHeader:reqInterfaceData.getReqBodys().getData()){
                    if(reqHeader.getValue().startsWith("$")) { //判断请求表单内容是否存在用例变量，表单jsonpath变量示例：$..[?(@.name=='Content-Length')].value
                        String data=JsonUtil.jsonPathRead(httpResponseData.getRespText().toString(),reqHeader.getValue());
                        if(data!=null) reqHeader.setValue(data);
                    }
                }
            }
            LogUtil.info("正常body参数变量替换后："+JSON.toJSONString(reqInterfaceData.getReqBodys().getData()));
        }

        //存在前置用例时，替换正常用例中填入的前置参数变量
        if(preHttpResponseData!=null){
            for(ReqHeader reqHeader: reqInterfaceData.getReqHeaders()){
                if(reqHeader.getValue().startsWith("#")){ //判断请求头是否存在前置用例变量，区分前一个接口和前置用例变量
                    String data=replaceResponseHeaders(preHttpResponseData.getRespHeaders(),reqHeader.getValue().substring(1));
                    if(data!=null) reqHeader.setValue(data);
                }
                if(reqHeader.getValue().startsWith("$")) { //请求头从前置用例接口的响应数据中获取，配置jsonpath解析表达式
                    String data=JsonUtil.jsonPathRead(preHttpResponseData.getRespText().toString(),reqHeader.getValue());
                    if(data!=null) reqHeader.setValue(data);
                }
            }

            LogUtil.info("前置header参数变量替换后："+JSON.toJSONString(reqInterfaceData.getReqHeaders()));
            if(ConverterUtil.isJSON(preHttpResponseData.getRespText().toString())){
                for(ReqHeader reqHeader:reqInterfaceData.getReqBodys().getData()){
                    if(reqHeader.getValue().startsWith("$")) { //判断请求表单内容是否存在用例变量，表单jsonpath变量示例：$..[?(@.name=='Content-Length')].value
                        String data=JsonUtil.jsonPathRead(preHttpResponseData.getRespText().toString(),reqHeader.getValue());
                        if(data!=null) reqHeader.setValue(data);
                    }
                }
            }
            LogUtil.info("前置body参数变量替换后："+JSON.toJSONString(reqInterfaceData.getReqBodys().getData()));
        }
    }


    /**
     * @description 加密参数
     * @author 星空梦语
     * @date 2021/2/28 下午2:30
     */
    public void encryptValue(String projectCode,String moduleCode,ReqCaseData reqInterfaceData) throws Exception {
        //加密表单参数
        for(ReqHeader reqHeader:reqInterfaceData.getReqBodys().getData()){
            if(StringUtils.isNotBlank(reqHeader.getKey())){
                String data= cipherService.encrypt(projectCode,moduleCode,reqHeader.getValue());
                if(data!=null) reqHeader.setValue(data);
            }
        }

        //加密json格式参数
        if(StringUtils.isNotBlank(reqInterfaceData.getReqBodys().getJsonData())){
            String data= cipherService.encrypt(projectCode,moduleCode,reqInterfaceData.getReqBodys().getJsonData());
            if(data!=null) reqInterfaceData.getReqBodys().setJsonData(data);
        }

    }



    /**
     * @description  迭代遍历判断响应头
     * @author 星空梦语
     * @date 2021/2/17 下午10:26
     */
    public String replaceResponseHeaders(Object response, String param){
        Header[] respHeaders= (Header[]) response;
        for(Header header : respHeaders){
            if(header.getName().equals(param)){
                return header.getValue();
            }
        }
        return  null;
    }




    /**
     * @description 用例校验
     * @author 星空梦语
     * @date 2020/9/16 18:07
     */
    public  CaseLog verifResponseData(ReqRequest reqRequest,ReqCaseData reqCaseData, String response,int preType,String serviceType) throws Exception {
        CaseLog caseLog=new CaseLog();
        caseLog.setProjectCode(reqRequest.getProjectCode());
        caseLog.setModuleCode(reqRequest.getModuleCode());
        caseLog.setTestEnv(reqRequest.getEnv());
        caseLog.setCaseId(reqRequest.getId());
        caseLog.setPlanId(reqRequest.getPlanID());
        caseLog.setType(String.valueOf(preType));
        caseLog.setInterfaceDesc(reqCaseData.getReqInterface().getInterfaceDesc());
        caseLog.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
        if(response.length()>200){
            String text=response.substring(0,200);
            caseLog.setResponseData(text+"，...过长被截取");
        }else{
            caseLog.setResponseData(response);
        }
        String msg="";
        String type=reqCaseData.getReqVerifs().getType();
        boolean isflag=true;
        if(type.equals("none")){
            msg="当前用例id:"+reqRequest.getId()+" 无需校验，校验跳过";
            isflag=false;
        }
        if(type.equals("json")  && !ConverterUtil.isJSON(response)){
            msg="当前用例id:"+reqRequest.getId()+" 响应数据非json格式，校验失败";
            isflag=false;
        }
        if(!isflag){
            caseLog.setMsg(msg);
            caseLog.setResult("true");  //无校验数据或响应非json时暂时默认true
            LogUtil.err(msg);
            caseLogMapper.insertSelective(caseLog);
            return caseLog;
        }
        boolean isSuccess=true;
        List<VerifData> verifDataList=reqCaseData.getReqVerifs().getJsonData();  //校验类型为json
        List<VerifData> verifDataList1=new ArrayList<>();
        for(VerifData verifData:verifDataList){
            if(StringUtils.isBlank(verifData.getVerifKey())){
                LogUtil.info("校验类型为json,但是发现校验数据json  key为空，校验跳过");
                continue;
            }
            String path="$";
            if(verifData.getVerifKey().startsWith("$")){   //参数值以$开头的，直接可以解析
                path=verifData.getVerifKey();
            }else{
                if(StringUtils.isNotBlank(verifData.getVerifParentKey())){
                    path+=".."+verifData.getVerifParentKey();
                    if(StringUtils.isNotBlank(verifData.getVerifKey())){
                        path+="."+verifData.getVerifKey();
                    }
                }else{
                    if(StringUtils.isNotBlank(verifData.getVerifKey())){
                        path+=".."+verifData.getVerifKey();
                    }
                }
            }
            try{
                String text=JsonUtil.jsonPathRead(response,path);
                VerifData checkVerif=checkData(verifData,text);
                verifDataList1.add(checkVerif);
                if(!checkVerif.isResult()){
                    isSuccess=false;
                }
            }catch (Exception e){
                caseLog.setMsg("解析用例接口响应数据异常，"+e.getMessage());
                break;
            }
        }

        if(type.equals("all")){   //校验类型为全部校验时
            VerifData verifData=new VerifData();
            verifData.setVerifType("=");
            verifData.setVerifValue(reqCaseData.getReqVerifs().getData());
            VerifData checkVerif=checkData(verifData,response);
            verifDataList1.add(checkVerif);
        }
        caseLog.setVerifData(JSON.toJSONString(verifDataList1));
        caseLog.setResult(String.valueOf(isSuccess));
        caseLog.setServiceType(serviceType);
        caseLogMapper.insertSelective(caseLog);
        return  caseLog;
    }



    /**
     * @description 数据明细验证
     * @author 星空梦语
     * @date 2020/9/17 16:37
     */
    public VerifData checkData(VerifData verifData,String response){
        verifData.setResult(false);
        verifData.setActualValue(response);
        long actual=0,expect=0;
        try{
            switch (verifData.getVerifType()){
                case ">": //实际大于预期
                    if(ConverterUtil.isNumeric(response)){
                        actual= ConverterUtil.getAsNumber(response).longValue();
                        expect=ConverterUtil.getAsNumber(verifData.getVerifValue()).longValue();
                        if(actual>expect){
                            verifData.setResult(true);
                        }
                    }else{
                        verifData.setResult(false);
                        verifData.setMsg("校验失败，校验参数值："+response+" 非数值类型，无法进行>判断，本地校验跳过");
                    }
                    break;
                case "<": //实际小于预期
                    if(ConverterUtil.isNumeric(response)){
                        actual=ConverterUtil.getAsNumber(response).longValue();
                        expect=ConverterUtil.getAsNumber(verifData.getVerifValue()).longValue();
                        if(actual<expect){
                            verifData.setResult(true);
                        }
                    }else{
                        verifData.setResult(false);
                        verifData.setMsg("校验失败，校验参数值："+response+" 非数值类型，无法进行<判断，本地校验跳过");
                    }

                    break;
                case "=": //实际等于预期
                    if(ConverterUtil.isNumeric(response)){
                        actual=ConverterUtil.getAsNumber(response).longValue();
                        expect=ConverterUtil.getAsNumber(verifData.getVerifValue()).longValue();
                        if(actual==expect){
                            verifData.setResult(true);
                        }
                    }else{
                        if(verifData.getVerifValue().equals(response)){
                            verifData.setResult(true);
                        }
                    }
                    break;
                case "()": //实际包含预期
                    if(response.contains(verifData.getVerifValue())){
                        verifData.setResult(true);
                    }
                    break;
            }
        }catch (Exception e){
            verifData.setMsg(e.getMessage());
        }
        LogUtil.info("校验结果="+verifData.isResult()+"; key:"+verifData.getVerifKey()+" ； 预期结果："+verifData.getVerifValue()+" ;实际结果："+response);
        return verifData;
    }


    /**
     * @description  获取项目缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public int getTestCaseCache(){
        return (int) CacheUtil.getCacheObject("testCase");
    }


    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateTestCaseCache(){
        LogUtil.info("开始更新测试用例缓存");
        CacheUtil.removeCacheObject("testCase");
        int testCaseList= caseMapper.queryAllCount();
        CacheUtil.setCacheObject("testCase",testCaseList,0L);
    }



}
