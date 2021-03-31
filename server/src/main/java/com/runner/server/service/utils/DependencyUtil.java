package com.runner.server.service.utils;

import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.Params;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.version.Version;
import sun.rmi.runtime.Log;

import java.io.File;
import java.util.List;


/**
 * @author 星空梦语
 * @desc
 * @date 2021/3/21 下午4:37
 */
public class DependencyUtil {
    /**
     * 生成 RepositorySystem
     *
     * @return RepositorySystem
     */
    public static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }

    /**
     * 生成 RepositorySystemSession
     *
     * @param system RepositorySystem
     * @return RepositorySystemSession
     */
    public static RepositorySystemSession newSession(RepositorySystem system, String target) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = newLocalRepository(target);
        session.setConfigProperty(ConflictResolver.CONFIG_PROP_VERBOSE, true);
        session.setConfigProperty(DependencyManagerUtils.CONFIG_PROP_VERBOSE, true);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        return session;
    }

    /**
     * 生成 RemoteRepository
     *
     * @param params
     * @return
     */
    public static RemoteRepository newRemoteRepository(Params params) {
        RemoteRepository central=null;
        String repository = params.getRepository();
        String username = params.getUsername();
        String password = params.getPassword();
        if (StringUtils.isNotBlank(repository)) {
            if (StringUtils.isBlank(username) && StringUtils.isBlank(password)) {
                central = new RemoteRepository.Builder("central", "default", repository).build();
            } else {
                Authentication authentication = new AuthenticationBuilder().addUsername(username).addPassword(password).build();
                central = new RemoteRepository.Builder("central", "default", repository).setAuthentication(authentication).build();
            }
        }

        return central;
    }

    /**
     * 生成 LocalRepository
     *
     * @param target
     * @return
     */
    public static LocalRepository newLocalRepository(String target) {
        System.out.println(SystemUtils.getUserHome());
        return new LocalRepository(target);
    }


    public static DefaultArtifact newDefaultArtifact(String artifactId, String groupId, String version) {
        String artifactStr = groupId + ":" + artifactId + ":" + version;
        return new DefaultArtifact(artifactStr);
    }



    /**
     * 从指定maven地址下载指定jar包
     *
     * @param params
     * @throws
     */
    public static void downLoadMavenJar(Params params) throws Exception {
        LogUtil.info("准备下载maven 依赖jar ，参数："+ JSON.toJSONString(params));
        File file=new File(params.getTarget());
        if(!file.exists()){
            file.mkdirs();
        }
        //下载该jar包及其所有依赖jar包
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem, params.getTarget());
        RemoteRepository central = newRemoteRepository(params);
        DefaultArtifact defaultArtifact = newDefaultArtifact(params.getArtifactId(), params.getGroupId(), params.getVersion());
        Dependency dependency = new Dependency(defaultArtifact, null);
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.addRepository(central);
        DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();
        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot(node);
        repoSystem.resolveDependencies(session, dependencyRequest);
        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        node.accept(nlg);
        LogUtil.info("服务依赖jar下载列表："+JSON.toJSONString(nlg.getFiles()));

    }







}
