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
package net.nowina.cadmelia.ide.stl;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import net.nowina.cadmelia.stl.parser.ParseException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;

@MultiViewElement.Registration(
        displayName = "#LBL_STL_VISUAL",
        iconBase = "net/nowina/cadmelia/ide/stl/stl_mimetype.png",
        mimeType = "application/sla",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "STLVisual",
        position = 2000
)
@Messages("LBL_STL_VISUAL=Visual")
public final class STLVisualElement extends JPanel implements MultiViewElement {

    private static final Logger LOGGER = Logger.getLogger(STLVisualElement.class.getName());

    private STLDataObject obj;
    private JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;

    public STLVisualElement(Lookup lkp) {
        obj = lkp.lookup(STLDataObject.class);
        assert obj != null;

        JFXPanel panel = new JFXPanel();

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {

                    final Pane viewContainer = init(obj);
                    panel.setScene(new Scene(viewContainer));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

        });

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

    }

    private void setMeshScale(
            SolidMesh solidMesh, Bounds t1, final MeshView meshView) {

        double maxDim = Math.max(solidMesh.getSizeX(), Math.max(solidMesh.getSizeY(), solidMesh.getSizeZ()));
        double minContDim = Math.min(t1.getWidth(), t1.getHeight());
        double scale = minContDim / (maxDim * 2);
        meshView.setScaleX(scale);
        meshView.setScaleY(scale);
        meshView.setScaleZ(scale);
    }

    @Override
    public String getName() {
        return "STLVisualElement";
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    Pane init(STLDataObject obj) throws Exception {

        Group viewGroup = new Group();
        SubScene subScene = new SubScene(viewGroup, 100, 100, true, SceneAntialiasing.BALANCED);

        Pane viewContainer = new Pane();

        subScene.widthProperty().bind(viewContainer.widthProperty());
        subScene.heightProperty().bind(viewContainer.heightProperty());

        PerspectiveCamera subSceneCamera = new PerspectiveCamera(false);
        subScene.setCamera(subSceneCamera);

        viewContainer.getChildren().add(subScene);

        buildMeshAndDisplay(obj, solidMesh -> {

            final MeshView meshView = solidMesh.getMeshView();

            setMeshScale(solidMesh, viewContainer.getBoundsInLocal(), meshView);

            PhongMaterial m = new PhongMaterial(Color.YELLOW);

            meshView.setCullFace(CullFace.NONE);

            meshView.setMaterial(m);

            viewGroup.layoutXProperty().bind(viewContainer.widthProperty().divide(2));
            viewGroup.layoutYProperty().bind(viewContainer.heightProperty().divide(2));

            viewContainer.boundsInLocalProperty().addListener(
                    (ov, oldV, newV) -> {
                        setMeshScale(solidMesh, newV, meshView);
                    });

            viewContainer.addEventHandler(MouseEvent.ANY, new MouseRotationEventHandler(meshView, MouseButton.PRIMARY));

            viewGroup.getChildren().add(meshView);

            return null;

        });

        return viewContainer;

    }

    public void buildMeshAndDisplay(STLDataObject obj, Function<SolidMesh, Void> success) {

        RequestProcessor.getDefault().post(() -> {

            ProgressHandle progress = ProgressHandleFactory.createHandle("Viewing file");
            try {
                progress.start();
                progress.switchToIndeterminate();
                SolidMesh solidMesh = new SolidMesh(obj);
                
                Platform.runLater(() -> {
                    success.apply(solidMesh);
                });
                
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, null, e);
            } finally {
                progress.finish();
            }

        });

    }

}
