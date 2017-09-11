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
package net.nowina.cadmelia.ide.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.PrintWriter;
import net.nowina.cadmelia.construction.BuilderFactory;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.script.Script;
import net.nowina.cadmelia.script.ScriptScene;
import net.nowina.cadmelia.script.parser.ScriptParser;
import net.nowina.cadmelia.shape.jts_clipper.JTSClipperShapeBuilder;
import net.nowina.cadmelia.solid.bspcsg.FactoryBuilder;
import net.nowina.cadmelia.stl.STLWriter;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;

@ActionID(
        category = "Build",
        id = "net.nowina.cadmelia.ide.actions.RenderOpenSCADAction"
)
@ActionRegistration(
        iconBase = "net/nowina/cadmelia/ide/actions/openscad_mimetype.png",
        displayName = "#CTL_RenderOpenSCADAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/application/x-openscad/Actions", position = 150)
    ,
  @ActionReference(path = "Editors/application/x-openscad/Popup", position = 400, separatorAfter = 450)
})
@Messages("CTL_RenderOpenSCADAction=Render OpenSCAD")
public final class RenderOpenSCADAction implements ActionListener {

    private final DataObject context;

    public RenderOpenSCADAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(final ActionEvent ev) {

        final FileObject o = context.getPrimaryFile();

        RequestProcessor.getDefault().post(() -> {

            ProgressHandle progress = ProgressHandleFactory.createHandle("Rendering file " + o.getName());
            progress.start();
            progress.switchToIndeterminate();

            try {
                FileObject outFile = o.getParent().createData(o.getName() + ".stl");

                BuilderFactory.registerShapeBuilder(new JTSClipperShapeBuilder());
                BuilderFactory.registerSolidBuilder(new FactoryBuilder().build());
                BuilderFactory factory = BuilderFactory.getInstance();
                ScriptScene scene = new ScriptScene(factory);

                try (InputStream in = o.getInputStream();
                        PrintWriter out = new PrintWriter(outFile.getOutputStream())) {
                    ScriptParser parser = new ScriptParser(in);
                    Script script = parser.Script();
                    scene.executeScript(script);

                    STLWriter writer = new STLWriter();
                    writer.write((Solid) scene.getRoot(), out);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progress.finish();
            }

        });

    }

}
