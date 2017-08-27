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
package net.nowina.cadmelia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.nowina.cadmelia.construction.BuilderFactory;
import net.nowina.cadmelia.shape.jts_clipper.JTSClipperShapeBuilder;
import net.nowina.cadmelia.solid.bspcsg.FactoryBuilder;

import java.io.IOException;

public class CADmeliaEditorApp extends Application {

    public static void main(String[] args) {
        BuilderFactory.registerShapeBuilder(new JTSClipperShapeBuilder());
        BuilderFactory.registerSolidBuilder(new FactoryBuilder().build());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        defineStage(primaryStage);
    }

    public static void defineStage(Stage primaryStage) throws IOException {
        Parent main = loadFXML();

        Scene scene = new Scene(main, 1300, 768, true);

        PerspectiveCamera camera = new PerspectiveCamera();

        scene.setCamera(camera);

        primaryStage.getIcons().add(new Image("net/nowina/cadmelia/camelia_logo.png"));
        primaryStage.setTitle("CADmelia Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Parent loadFXML() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(CADmeliaEditorApp.class.getResource("EditorApp.fxml"));
        fxmlLoader.load();

        Parent root = fxmlLoader.getRoot();

        root.getStylesheets().add(CADmeliaEditorApp.class.getResource("EditorApp.css").
                toExternalForm());
        return root;
    }

}
