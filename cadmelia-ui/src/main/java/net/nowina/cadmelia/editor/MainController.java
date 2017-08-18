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
package net.nowina.cadmelia.editor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.nowina.cadmelia.CADmeliaEditorApp;
import net.nowina.cadmelia.construction.Solid;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private static final String SCAD_DESCRIPTION = "OpenSCAD files (*.scad)";
    private static final String SCAD_EXTENSION = "*.scad";
    private static final String ENCODING = "UTF-8";

    private final Group viewGroup = new Group();

    private final SCADEditor editor = new SCADEditor();

    private boolean autoCompile = true;

    private Solid solid;

    @FXML
    private TextArea logView;

    @FXML
    private ScrollPane editorContainer;

    @FXML
    private Pane viewContainer;

    private SubScene subScene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        editor.replaceText("sphere(1)");

        editorContainer.setContent(editor);

        subScene = new SubScene(viewGroup, 100, 100, true,
                SceneAntialiasing.BALANCED);

        subScene.widthProperty().bind(viewContainer.widthProperty());
        subScene.heightProperty().bind(viewContainer.heightProperty());

        PerspectiveCamera subSceneCamera = new PerspectiveCamera(false);
        subScene.setCamera(subSceneCamera);

        viewContainer.getChildren().add(subScene);

        TextViewConsole console = new TextViewConsole(logView);
        System.setOut(new PrintStream(console));

    }

    private void setCode(String code) {
        editor.replaceText(code);
    }

    private String getCode() {
        return editor.getText();
    }

    private void clearLog() {
        logView.setText("");
    }

    private CompilationService compilationService;

    private void compile(boolean preview) {

        solid = null;

        clearLog();

        viewGroup.getChildren().clear();

        try {

            compilationService = new CompilationService();
            compilationService.setPreview(preview);
            compilationService.setCode(getCode());
            compilationService.setOnFailed((event) -> {
                StringWriter writer = new StringWriter();
                PrintWriter p = new PrintWriter(writer);
                event.getSource().getException().printStackTrace(p);
                logView.appendText(writer.toString());
            });
            compilationService.setOnSucceeded((event) -> {

                Solid solid = (Solid) event.getSource().getValue();
                this.solid = solid;

                SolidMesh solidMesh = new SolidMesh(solid);

                final MeshView meshView = solidMesh.getMeshView();

                setMeshScale(solidMesh,
                        viewContainer.getBoundsInLocal(), meshView);

                PhongMaterial m = new PhongMaterial(Color.YELLOW);

                meshView.setCullFace(CullFace.NONE);

                meshView.setMaterial(m);

                viewGroup.layoutXProperty().bind(
                        viewContainer.widthProperty().divide(2));
                viewGroup.layoutYProperty().bind(
                        viewContainer.heightProperty().divide(2));

                viewContainer.boundsInLocalProperty().addListener(
                        (ov, oldV, newV) -> {
                            setMeshScale(solidMesh, newV, meshView);
                        });

                viewContainer.addEventHandler(MouseEvent.ANY, new MouseRotationEventHandler(meshView, MouseButton.PRIMARY));

                viewGroup.getChildren().add(meshView);
            });

            compilationService.start();

        } catch (Exception e) {
            logView.setText(e.getMessage());
        }

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

    @FXML
    private void onNew(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        CADmeliaEditorApp.defineStage(stage);
        stage.show();
    }

    @FXML
    private void onLoadFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CADmeliaEditorApp File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(SCAD_DESCRIPTION, SCAD_EXTENSION));

        File f = fileChooser.showOpenDialog(null);
        if (f != null) {
            try (InputStream in = new BufferedInputStream(new FileInputStream(f))) {
                setCode(IOUtils.toString(in, ENCODING));
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        }

    }

    @FXML
    private void onSaveFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CADmeliaEditorApp File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(SCAD_DESCRIPTION, SCAD_EXTENSION));

        File f = fileChooser.showSaveDialog(null);
        if (f != null) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(f))) {
                out.write(getCode().getBytes(ENCODING));
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        }

    }

    @FXML
    private void onExportAsSTLFile(ActionEvent e) {

        if (solid == null) {
            System.out.println("Cannot export STL. There is no geometry :(");
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export STL File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "STL files (*.stl)",
                        "*.stl"));

        File f = fileChooser.showSaveDialog(null);
        if (f != null) {
            WriteSTLService service = new WriteSTLService();
            service.setSolid(solid);
            service.setOutputFile(f);
            service.start();
        }

    }

    @FXML
    private void onCompileAndRun(ActionEvent e) {
        compile(false);
    }

    @FXML
    private void onPreview(ActionEvent e) {
        compile(true);
    }

    @FXML
    private void onClose(ActionEvent e) {
        Stage stage = (Stage) editor.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onQuit(ActionEvent e) {
        Platform.exit();
    }

    @FXML
    private void onAutoCompile(ActionEvent e) {
        autoCompile = !autoCompile;
    }

    @FXML
    private void onRedo(ActionEvent e) {
        editor.redo();
    }

    @FXML
    private void onUndo(ActionEvent e) {
        editor.undo();
    }

}
