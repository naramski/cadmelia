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

import javafx.embed.swing.JFXPanel;
import org.junit.Test;

import javax.swing.*;

public class SampleTest {

    @Test
    public void testWheel() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/wheel.scad", "build/wheel.stl"});
    }

    @Test
    public void testSampleScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/sample.scad", "build/sample-script.stl"});
    }

    @Test
    public void testBatteryHolderScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/battery-holder.scad", "build/battery-holder-script.stl"});
    }

    @Test
    public void testCone() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/cone.scad", "build/cone-test.stl"});
    }

    @Test
    public void testCylinder() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/cyl.scad", "build/cylinder-test.stl"});
    }

    @Test
    public void testCylinderRotate() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/cyl-rotate.scad", "build/cylinder-rotate-test.stl"});
    }

    @Test
    public void testCylinderUnion() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/cyl-union.scad", "build/cylinder-union-test.stl"});
    }

    @Test
    public void testSpheresPlaneScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/spheres-plane.scad", "build/spheres-plane.stl"});
    }

    @Test
    public void testEggScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/egg.scad", "build/egg-script.stl"});
    }

    @Test
    public void testHingeScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/hinge.scad", "build/hinge-script.stl"});
    }

    @Test
    public void testHingeWithoutHullScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/hinge-without-hull.scad", "build/hinge-without-hull-script.stl"});
    }

    @Test
    public void testLogoScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/logo.scad", "build/logo-script.stl"});
    }

    @Test
    public void testMicroSDScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/micro-sd-card.scad", "build/micro-sd-card-script.stl"});
    }

    @Test
    public void testMoldScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/mold.scad", "build/mold-script.stl"});
    }

    @Test
    public void testPlaneWithHolesScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/plane-with-holes.scad", "build/plane-with-holes-script.stl"});
    }

    @Test
    public void testServoHeadScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/servo-head-male.scad", "build/servo-head-male-script.stl"});
    }

    @Test
    public void testServoFemaleScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/servo-head-female.scad", "build/servo-head-female-script.stl"});
    }

    @Test
    public void testServoWheelScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/servo-wheel.scad", "build/servo-wheel-script.stl"});
    }

    @Test
    public void testSphereScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/sphere.scad", "build/test-sphere.stl"});
    }

    @Test
    public void testSpheresScript() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/spheres.scad", "build/spheres-script.stl"});
    }

    @Test
    public void testSurfacePenHolder() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/samples/surface-pro-2-pen-holder.scad", "build/surface-pro-2-pen-holder-script.stl"});
    }

    @Test
    public void testUnionThreeSpheres() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/union-three-spheres.scad", "build/union-three-spheres.stl"});
    }

    @Test
    public void testHelloWorld() throws Exception {
        CompileToSTLApp.main(new String[]{"main", "src/test/resources/scripts/helloworld.scad", "build/helloworld.stl"});
    }

}
