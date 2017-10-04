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

import net.nowina.cadmelia.construction.Construction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;

public class OpenSCADSampleTest {

    private CompileToSTLApp app;

    @Before
    public void setup() {
        app = new CompileToSTLApp();
    }

    @Test
    public void testComment1() throws Exception {

        app.render(new StringReader("// should do nothing"));

        // Should not have any polygon
        Assert.assertNull(app.getScene().getRoot());

    }

    @Test
    public void testComment2() throws Exception {

        app.render(new StringReader("/* should do nothing */"));

        // Should not have any polygon
        Assert.assertNull(app.getScene().getRoot());

    }

    @Test
    public void testComment3() throws Exception {

        app.render(new StringReader("/* \n should do nothing \n */"));

        // Should not have any polygon
        Assert.assertNull(app.getScene().getRoot());

    }

    @Test
    public void testComment4() throws Exception {

        app.render(new StringReader("/* \n should \n do /* nothing \n */"));

        // Should not have any polygon
        Assert.assertNull(app.getScene().getRoot());

    }

    @Test
    public void testEcho() throws Exception {

        app.render(new StringReader("echo(\"The quick brown fox \\tjumps \\\"over\\\" the lazy dog.\\rThe quick brown fox.\\nThe \\\\lazy\\\\ dog.\");"));

    }

    @Test
    public void testText() throws Exception {

        app.setOutput(new File("build/base-text.stl"));
        app.render(new StringReader("text(\"OpenSCAD\");"));

    }

    @Test
    public void testCube() throws Exception {

        app.setOutput(new File("build/base-cube.stl"));
        app.render(new StringReader("cube([18,28,8],true);"));

    }

    @Test
    public void testRotation() throws Exception {

        app.setOutput(new File("build/base-rotation.stl"));
        app.render(new StringReader("cylinder(h=10); translate([10,0,0]) rotate([90,0,0]) cylinder(h=10);translate([20,0,0]) rotate([0,90,0]) cylinder(h=10); translate([35,0,0]) rotate([0,0,90]) cylinder(h=10); translate([50,0,0]) rotate([0,0,90]) rotate([0,90,0]) cylinder(h=10); "));

    }

}
