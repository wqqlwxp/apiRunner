package com.runner.server.dao.entity.bo;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/3/21 下午4:38
 */
public class Params {

    private String groupId;
    private String artifactId;
    private String version;
    private String repository;
    private String target;
    private String username=null;
    private String password=null;


    public Params(String target) {
        super();
        this.target = target;
    }



    public Params(String groupId, String artifactId) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public Params(String groupId, String artifactId,String version) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version=version;
    }




    public Params(String groupId, String artifactId, String username,
                  String password) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.username = username;
        this.password = password;
    }



    public Params(String groupId, String artifactId, String version,
                  String repository, String target, String username, String password) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repository = repository;
        this.username = username;
        this.password = password;
        this.target=target;
    }



    public Params(String groupId, String artifactId, String version,
                  String username, String password) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.username = username;
        this.password = password;
    }



    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getArtifactId() {
        return artifactId;
    }
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getRepository() {
        return repository;
    }
    public void setRepository(String repository) {
        this.repository = repository;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
