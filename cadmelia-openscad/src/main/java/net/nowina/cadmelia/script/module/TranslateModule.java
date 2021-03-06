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
package net.nowina.cadmelia.script.module;

import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(TranslateModule.class);

    public static final String MODULE_NAME = "translate";
    public static final String VECTOR_PARAM = "v";

    public TranslateModule() {
        super(MODULE_NAME);
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Construction composition = super.execute(op, context);
        if(composition == null) {
            LOGGER.warn("Cannot execute operation " + op + " on null");
            return null;
        }

        Expression translateExpr = op.getArg(VECTOR_PARAM);
        if (translateExpr == null) {
            translateExpr = op.getFirstUnamedArg();
        }

        Vector translation = translateExpr.evaluateAsVector(context);

        LOGGER.info("Translate composition of " + translation);
        composition = composition.translate(translation);
        LOGGER.info("Translated composition " + composition);
        return composition;
    }

}
