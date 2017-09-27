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
    public void union1() throws Exception {
        app.setOutput(new File("build/testCup-union.stl"));
        app.render(new StringReader("union() { cylinder(r1=4,r2=66.4633,h=63.5,$fn=6); translate([0,0,-.05]) cylinder(r1=0,r2=62.4633,h=63.6,$fn=6); }"));
    }

    @Test
    public void difference2() throws Exception {
        app.setOutput(new File("build/testCup-difference.stl"));
        app.render(new StringReader("difference() { cylinder(r1=4,r2=66.4633,h=63.5,$fn=6); translate([0,0,-.05]) cylinder(r1=1,r2=62.4633,h=63.6,$fn=6); }"));
    }

}
