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
package net.nowina.cadmelia.ide.openscad.lexer;

import java.util.Arrays;
import java.util.List;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author david
 */
public class OpenSCADLexer implements Lexer<OpenSCADTokenId> {

    private final LexerRestartInfo<OpenSCADTokenId> info;

    OpenSCADLexer(LexerRestartInfo<OpenSCADTokenId> info) {
        this.info = info;
    }

    @Override
    public Token<OpenSCADTokenId> nextToken() {

        OpenSCADTokenId id = null;
        id = getNextToken();

        if (info.input().readLength() < 1) {
            return null;
        }
        
        return info.tokenFactory().createToken(id);
    }

    private OpenSCADTokenId getNextToken() {
        OpenSCADTokenId id = null;
        int c = info.input().read();
        switch(c) {
            case '(':
            case ')':
            case '{':
            case '}':
            case '=':
            case '-':
            case '+':
            case '*':
                id = OpenSCADTokenId.OPERATOR;
                break;
            case '/':
                c = info.input().read();
                if(c == '/') {
                    readLineComment();
                    id = OpenSCADTokenId.LINE_COMMENT;
                    break;
                } else {
                    info.input().backup(1);
                    id = OpenSCADTokenId.OPERATOR;
                    break;
                }
            case ' ':
            case '\t':
            case '\n':
            case '\r':
                id = OpenSCADTokenId.WHITESPACE;
                break;
            case LexerInput.EOF:
                id = OpenSCADTokenId.EOF;
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append((char)c);
                if(isCharacter(c)) {
                    readString(sb);
                    if(isKeyword(sb.toString())) {
                        id = OpenSCADTokenId.KEYWORD;
                        break;
                    }
                }
                id = OpenSCADTokenId.CHARACTER;
                break;
        }
        return id;
    }

    public void readString(StringBuilder sb) {
        int c = -1;
        while(isCharacter(c = info.input().read())) {
            sb.append((char) c);
        }
        info.input().backup(1);
    }
    
    public void readLineComment() {
        int c = -1;
        while(!isEndOLine(c = info.input().read()));
        info.input().backup(1);
    }
    
    public boolean isCharacter(int c) {
        return ('a' <= c && 'z' >= c) || ('A' <= c && 'Z' >= c);
    }
    
    public boolean isEndOLine(int c) {
        return c == '\n' || c == LexerInput.EOF;
    }
    
    public boolean isKeyword(String string) {
        List<String> keywords = Arrays.asList(new String[] { "true", "difference", "union", "intersection", "false", 
        "linear_extrude", "rotate", "translate", "module", "offset" });
        return keywords.contains(string);
    }
    
    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }

}