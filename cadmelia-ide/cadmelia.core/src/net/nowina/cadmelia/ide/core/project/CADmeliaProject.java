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

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author david
 */
public class CADmeliaProject implements Project {

    private final FileObject projectDirectory;

    private final ProjectState projectState;

    private Lookup lookup;

    public CADmeliaProject(FileObject projectDirectory, ProjectState projectState) {
        this.projectDirectory = projectDirectory;
        this.projectState = projectState;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDirectory;
    }

    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            lookup = Lookups.fixed(new Object[]{
                new CADmeliaProjectInfo()
            });
        }
        return lookup;
    }

    public class CADmeliaProjectInfo implements ProjectInformation {

        @StaticResource()
        public static final String PROJECT_ICON = "net/nowina/cadmelia/ide/core/project/cadmelia_logo_16.png";

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(PROJECT_ICON));
        }

        @Override
        public Project getProject() {
            return CADmeliaProject.this;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
        }

    }

}
