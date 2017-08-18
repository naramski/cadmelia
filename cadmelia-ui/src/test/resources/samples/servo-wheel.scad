clear = 0.3;

toothLength = 0.7;
toothWidth = 0.1;
toothHeight = 0.3;
toothCount = 25;
headHeight = 4;
headDiameter = 5.92;
headScrewDiameter = 2.5;
headThickness = 1.1;

numberOfArms = 3;
innerWidth = 7;
outerWidth = 3.5;
thickness = 2;
radius = 40;
ringThickness = 3;
wheelThickness = 5;

minorArmLength = radius * 0.75;
minorArmHeight = headHeight;
minorArmThickness = 2.5;

outerRingThickness = wheelThickness/3.0*2;
outerRingDepth = 0.5;

dt = 360.0 / numberOfArms;

module servoArm(innerWidth, outerWidth, thickness, radius, wheelThickness, minorArmThickness, minorArmLength, minorArmHeight) {

    linear_extrude(height = thickness)
        polygon(points = [
            [ -innerWidth * 0.5, 0 ],
            [ innerWidth * 0.5, 0 ],
            [ outerWidth * 0.5, radius - wheelThickness ],
            [ -outerWidth * 0.5, radius - wheelThickness ]
        ]);

        rotate(a = [ 0, 0, -90 ])
            translate(v = [ 0, 0, -minorArmThickness * 0.5])
                rotate(a = [ -90, 0, 0 ])
                    linear_extrude(height = minorArmThickness)
                        polygon(points = [
                            [ headDiameter * 0.5 + headThickness * 0.5, thickness ],
                            [ minorArmLength - headDiameter * 0.5 - headThickness * 0.5, thickness ],
                            [ headDiameter * 0.5 + headThickness * 0.5, minorArmHeight + thickness * 0.5 ]
                        ]);

}

radius2 = headDiameter / 2 - toothHeight + clear;

difference() {

    union() {
        difference() {

            union() {

                // SERVO-HEAD-FEMALE
                translate(v = [ 0, 0, headHeight ])
                rotate(a = [ 180, 0, 0 ])
                difference() {

                    cylinder(r1 = headDiameter / 2 + headThickness, r2 = headDiameter / 2 + headThickness, h = headHeight + 1, center=false);

                    cylinder(r1 = headScrewDiameter / 2, r2 = headScrewDiameter / 2, h = 10, center=false);

                    union() {
                        cylinder(r1 = radius2 + 0.03, r2 = radius2 + 0.03, h = headHeight, center = false, $fa = toothCount * 2);

                        for (i = [ 0 : toothCount-1 ]) {
                            rotate( a = [ 0, 0, i * (360.0 / toothCount)]) {
                                translate(v = [0, radius2, 0 ]) {
                                    linear_extrude(height = headHeight) {
                                        polygon(points = [
                                            [ -toothLength / 2, 0 ],
                                            [ toothLength / 2, 0 ],
                                            [ toothWidth / 2, toothHeight ],
                                            [ -toothWidth / 2, toothHeight ]
                                        ]);
                                    };
                                };
                            };
                        };
                    };

                };

                // ARMS
                for (i = [ 0 : numberOfArms - 1 ]) {
                    rotate(a = [ 0, 0, dt * i ])
                        servoArm(innerWidth = innerWidth, outerWidth = outerWidth, thickness = thickness,
                        radius = radius, wheelThickness = ringThickness, minorArmThickness = minorArmThickness,
                        minorArmLength = minorArmLength, minorArmHeight = minorArmHeight);
                };

            };

            cylinder(r1 = headScrewDiameter / 2.0, r2 = headScrewDiameter / 2.0, h = ringThickness * 2, center=false);

        };

        difference() {
            cylinder(r1 = radius, r2 = radius, h = wheelThickness, center=false, $fa=40);
            cylinder(r1 = radius - ringThickness, r2 = radius - ringThickness, h = wheelThickness, center=false, $fa=40);
        };

    };

    translate(v = [ 0, 0, wheelThickness*0.5-outerRingThickness*0.5])
        difference() {
            cylinder(r1 = radius, r2 = radius, h = outerRingThickness, center=false, $fa=40);
            cylinder(r1 = radius - outerRingDepth, r2 = radius - outerRingDepth, h = outerRingThickness, center=false, $fa=40);
        };

};