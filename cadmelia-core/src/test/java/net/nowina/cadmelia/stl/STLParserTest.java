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
package net.nowina.cadmelia.stl;

import net.nowina.cadmelia.stl.parser.STLParser;
import net.nowina.cadmelia.stl.parser.STLParserConstants;
import net.nowina.cadmelia.stl.parser.Token;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;

public class STLParserTest {

    @Test
    public void testParser() throws Exception {
        STLParser parser = new STLParser(new StringReader("vertex 1.2 0.2 -3"));
        verifyVertex(parser, "1.2", "0.2", "-3");

        parser = new STLParser(new StringReader("vertex 1.2 -0.2 -3e3"));
        verifyVertex(parser, "1.2", "-0.2", "-3e3");

        parser = new STLParser(new StringReader("vertex 1.0e2 -0.2e35 -3e-3"));
        verifyVertex(parser, "1.0e2", "-0.2e35", "-3e-3");

        parser = new STLParser(new StringReader("vertex 1.0e2 -0.2e35 -3e-3"));
        parser.Vertex();
    }

    private void verifyVertex(STLParser parser, String v1, String v2, String v3) {
        Token token = parser.getNextToken();
        Assert.assertEquals(STLParserConstants.VERTEX, token.kind);
        token = parser.getNextToken();
        Assert.assertEquals(STLParserConstants.NUMBER, token.kind);
        Assert.assertEquals(v1, token.image);
        Double.valueOf(token.image);
        token = parser.getNextToken();
        Assert.assertEquals(STLParserConstants.NUMBER, token.kind);
        Assert.assertEquals(v2, token.image);
        Double.valueOf(token.image);
        token = parser.getNextToken();
        Assert.assertEquals(STLParserConstants.NUMBER, token.kind);
        Assert.assertEquals(v3, token.image);
        Double.valueOf(token.image);
    }

    @Test
    public void testCube() throws Exception {
        try (FileInputStream input = new FileInputStream(new File("src/test/resources/cube.stl"))) {
            STLParser parser = new STLParser(input);
            parser.Solid();
        }
    }

    @Test
    public void testSphere() throws Exception {
        try (FileInputStream input = new FileInputStream(new File("src/test/resources/sphere.stl"))) {
            STLParser parser = new STLParser(input);
            parser.Solid();
        }
    }

    @Test
    public void test2Objects() throws Exception {
        try (FileInputStream input = new FileInputStream(new File("src/test/resources/2objects.stl"))) {
            STLParser parser = new STLParser(input);
            parser.Solid();
        }
    }

}
