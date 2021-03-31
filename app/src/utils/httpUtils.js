import axios from 'axios';

//post
export const  post = (url, data) => {
    return  axios({
        method: 'post',
        withCredentials: true,
        url:url,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
        },
        data: data
    })
};


export  const queryBaseProject = async function(offset,limit,code){
    let var1={},pageObject={},data=[];
    var1['projectCode']=code;
    pageObject["offset"]=offset;
    pageObject["limit"]=limit;
    pageObject["data"]=var1;
    return post('api/project/queryAllProject',JSON.stringify(pageObject))
};



export  const queryBaseModule = async function(offset,limit,code){
    let var1={}, pageObject={},data=[];
    var1['moduleCode']=code;
    pageObject["offset"]=offset;
    pageObject["limit"]=limit;
    pageObject["data"]=var1;
    return post('api/project/queryAllProjectModule',JSON.stringify(pageObject))
};

export  const queryBaseEnv= async function(offset,limit,projectCode){
    let var1={}, pageObject={},data=[];
    var1['projectCode']=projectCode;
    pageObject["offset"]=offset;
    pageObject["limit"]=limit;
    pageObject["data"]=var1;
    return post('api/env/queryAllEnvMachine',JSON.stringify(pageObject))
};

