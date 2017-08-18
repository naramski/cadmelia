jointLength = 10;
jointRadius = 5;
coneLength = 5;
jointHoleLength = 5;
jointConnectionThickness = jointRadius * 0.5;
resolution = 16;

difference() {

    hull() {

        translate(v = [ 0, 0, jointLength * 0.5 ])
            cylinder(h = coneLength, r1 = jointRadius, r2 = 0, center=false);

        translate(v = [ 0, 0, -jointLength * 0.5 ])
            rotate(a = [ 180, 0, 0 ])
                cylinder(h = coneLength, r1 = jointRadius, r2 = 0, center=false);

    };

    translate(v = [ jointConnectionThickness, 0, 0 ])
        cube(size = [ jointRadius * 2, jointHoleLength * 2, jointHoleLength ], center=true);

};
