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
package net.nowina.cadmelia.openscad.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author david
 */
public class OpenSCADLanguageHierarchy extends LanguageHierarchy<OpenSCADTokenId> {
    
    private static List<OpenSCADTokenId> tokens;
    private static Map<Integer, OpenSCADTokenId> idToToken;

    private static void init() {
        tokens = new ArrayList<>();
        idToToken = new HashMap<>();
        for(OpenSCADTokenId id : OpenSCADTokenId.values()) {
            idToToken.put(id.ordinal(), id);
            tokens.add(id);
        }
    }

    @Override
    protected synchronized Collection<OpenSCADTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<OpenSCADTokenId> createLexer(LexerRestartInfo<OpenSCADTokenId> info) {
        return new OpenSCADLexer(info);
    }

    @Override
    protected String mimeType() {
        return "application/x-openscad";
    }
    
}
