# CADmelia

CADmelia is an open-source CAD tool for creating 3D CAD models. It focus on the CAD aspects of
3D modelling, and the goal is to help creating 3D printed objects (CAM : Computer Aided Manufacturing)

Like OpenSCAD it is not an interactive modeller. Instead it is something like a 3D-compiler that
reads in a script file that describes the object and renders the 3D model from this script file.
This gives you (the designer) full control over the modelling process and enables you to easily change
any step in the modelling process or make designs that are defined by configurable parameters.

Two modelling techniques are provided :

- Constructive Solid Geometry (CSG)
- Extrusion of 2D shapes

The model can be exported in STL format.

![src/test/resources/samples/logo.scad](screenshots/logo_openSCAD.png)

![src/test/resources/samples/servo-head-female.scad](screenshots/servo_female.png)

CADmelia uses the modeling language defined by OpenSCAD (see [The_OpenSCAD_Language]
(https://en.wikibooks.org/wiki/OpenSCAD_User_Manual/The_OpenSCAD_Language)).

![http://www.openscad.org/cheatsheet/index.html](screenshots/openSCAD_cheat_sheet.png)

It is free software, licensed under EUPL.

# Usage (Command Line)

To compile a OpenSCAD script, the command line is :

    java -jar cadmelia-cli-<version>.jar <openscad.script> <output.stl>

# How to Build

The build requires Maven and Java => 1.8.

# License

This software are all the components are licensed under the EUPL license.

```
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
```

This software include two classes from the project F(X)yz under BSD license (https://github.com/naramski/FXyz).
The classes LineSegment and Text3DHelper contain the original header and package.
