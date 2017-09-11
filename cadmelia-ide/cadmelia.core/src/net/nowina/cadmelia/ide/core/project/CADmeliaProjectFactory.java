/*
 * Copyright 2017 David Naramski.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *      http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nowina.cadmelia.ide.core.project;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author david
 */
@ServiceProvider(service=ProjectFactory.class)
public class CADmeliaProjectFactory implements ProjectFactory {

    private static final String CADMELIA_MAIN_FILE = "cadmelia-project.xml";
    
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(CADMELIA_MAIN_FILE) != null;
    }

    @Override
    public Project loadProject(FileObject projectDirectory, ProjectState state) throws IOException {
        return new CADmeliaProject(projectDirectory, state);
    }

    @Override
    public void saveProject(Project project) throws IOException, ClassCastException {
    }
    
}
