toothLength = 0.7;
toothWidth = 0.1;
toothHeight = 0.3;
toothCount = 25;
headHeight = 4;
headDiameter = 5.92;
headScrewDiameter = 2.5;
headThickness = 1.1;
clear = 0.3;

radius = headDiameter / 2 - toothHeight + clear;

cylinder(r1 = radius + 0.03, r2 = radius + 0.03, h = headHeight, center = false, $fa = toothCount * 2);

for (i = [ 0 : toothCount-1 ]) {
    rotate( a = [ 0, 0, i * (360.0 / toothCount)]) {
        translate(v = [0, radius, 0 ]) {
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



