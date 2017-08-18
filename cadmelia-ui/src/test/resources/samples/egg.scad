radius = 22;
stretch = 1.50;
resolution = 64;

union() {
    intersection() {
        scale(v = [1, 1, stretch]) sphere(r = radius, $fa = 64);

        translate(v = [ 0, 0, stretch*radius])
            cube(2*stretch*radius, center=true);
    };

    scale(v = [1, 1, stretch*0.72])
        difference() {

            sphere(r = radius, $fa = 64);

            translate(v = [ 0, 0, stretch*radius])
                cube(2*stretch*radius, center=true);
        };

}
