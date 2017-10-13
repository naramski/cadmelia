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

import net.nowina.cadmelia.construction.FactoryBuilder;
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PolygonModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolygonModule.class);

    public static final String MODULE_NAME = "polygon";

    private FactoryBuilder factory;

    public PolygonModule(FactoryBuilder factory) {
        super(MODULE_NAME);
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Expression listExpression = op.getArg("points");
        if(listExpression == null) {
            listExpression = op.getFirstUnamedArg();
        }
        List<Vector> list = (List<Vector>) listExpression.evaluate(context);
        return polygon(list, context);

    }

    private Construction polygon(List<Vector> list, ScriptContext context) {
        return factory.createShapeFactory().polygon(list);
    }

}
