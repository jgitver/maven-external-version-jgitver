/**
 * Copyright (C) 2016 Matthieu Brouillard [http://oss.brouillard.fr/maven-external-version-jgitver] (matthieu@brouillard.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.brouillard.oss.maven.external.jgit;

import java.util.Optional;

import org.apache.maven.project.MavenProject;
import org.apache.maven.version.strategy.ExternalVersionException;
import org.apache.maven.version.strategy.ExternalVersionStrategy;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Configuration;

import fr.brouillard.oss.jgitver.GitVersionCalculator;

@Component(role = ExternalVersionStrategy.class, hint = "jgitver")
public class JGitverStrategy implements ExternalVersionStrategy { 
    @Configuration(name = "nonQualifierBranches", value = "master")
    String nonQualifierBranches;
    @Configuration(name = "appendGitCommitId", value = "false")
    Boolean appendGitCommitId;
    @Configuration(name = "gitCommitIdLength", value = "8")
    Integer gitCommitIdLength;
    
    @Override
    public String getVersion(MavenProject mavenProject) throws ExternalVersionException {
        GitVersionCalculator calculator = GitVersionCalculator.location(mavenProject.getBasedir());
        
        calculator.setAutoIncrementPatch(true);
        calculator.setUseDistance(true);
        
        calculator.setNonQualifierBranches(Optional.ofNullable(nonQualifierBranches).orElse("master"));
        calculator.setUseGitCommitId(Optional.ofNullable(appendGitCommitId).orElse(Boolean.FALSE));
        calculator.setGitCommitIdLength(Optional.ofNullable(gitCommitIdLength).orElse(8));
        
        return calculator.getVersion();
    }
}
