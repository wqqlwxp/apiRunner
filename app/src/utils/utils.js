export const isNotEmpty = (args)=> {
    if (args == null || args == '' || args == undefined) {
        return false;
    }
    return true;
};



export const  filterProjectByCode= (param,projectData)=>{
    let resp='';
    projectData.filter(function (obj) {
        if (obj.projectCode == param) {
            resp= obj.projectName
        }
    });
    return resp
};

export const  filterModuleByCode=(param,moduleAllData)=> {
    let resp='';
    moduleAllData.filter(function (obj) {
        if (obj.moduleCode == param) {
            resp= obj.moduleName
        }
    });
    return resp
};



export const filterEnvByCode=(row,param,envAllData)=> {
    let resp='';
    envAllData.filter(function (obj) {
        if (obj.envCode == param) {
            if(row.projectCode==obj.projectCode) {   //环境配置筛选根据项目决定
                resp= obj.envName
            }
        }
    });
    return resp
};


export const queryAuthName=()=>{
    return localStorage.getItem("nick_name")
};