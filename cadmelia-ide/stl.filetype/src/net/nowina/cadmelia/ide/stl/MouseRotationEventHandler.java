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

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public class MouseRotationEventHandler implements EventHandler<MouseEvent> {

    private double anchorAngleX;
    private double anchorAngleY;
    private double anchorX;
    private double anchorY;
    private final Rotate rotateX = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
    private final Rotate rotateZ = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
    private MouseButton btn;

    public MouseRotationEventHandler(Node n, MouseButton btn) {

        Bounds bounds = n.getLayoutBounds();

        n.getTransforms().addAll(rotateX, rotateZ);
        this.btn = btn;

        if (btn == null) {
            this.btn = MouseButton.MIDDLE;
        }

    }

    @Override
    public void handle(MouseEvent t) {
        if (!btn.equals(t.getButton())) {
            return;
        }

        t.consume();

        if (MouseEvent.MOUSE_PRESSED.equals(t.getEventType())) {
            anchorX = t.getSceneX();
            anchorY = t.getSceneY();
            anchorAngleX = rotateX.getAngle();
            anchorAngleY = rotateZ.getAngle();
            t.consume();
        } else if (MouseEvent.MOUSE_DRAGGED.equals(t.getEventType())) {
            rotateZ.setAngle(anchorAngleY + (anchorX - t.getSceneX()) * 0.7);
            rotateX.setAngle(anchorAngleX - (anchorY - t.getSceneY()) * 0.7);
        }

    }
}
