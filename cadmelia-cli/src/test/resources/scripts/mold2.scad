
module flower(size=10, expand=20) {
    circle(expand);
    translate(v = [expand,0,0]) circle(size);
    rotate(a = [0, 0, 60]) translate(v = [expand,0,0]) circle(size);
    rotate(a = [0, 0, 120]) translate(v = [expand,0,0]) circle(size);
    rotate(a = [0, 0, 180]) translate(v = [expand,0,0]) circle(size);
    rotate(a = [0, 0, 240]) translate(v = [expand,0,0]) circle(size);
    rotate(a = [0, 0, 300]) translate(v = [expand,0,0]) circle(size);
}

module mold(thickness = 1, radius = 10, expend = 20) {
    difference() {
        offset(r=thickness/2) {
            flower(20, radius, expand);
        };
        offset(r=-thickness/2) {
            flower(20, radius, expand);
        };
    };
}

height = 30;
factor = 1.8;
length = 57;
thickness = 1;

radius = length / ( 2 * (1 + factor) );
expand = radius * factor;
h = height / 4;

linear_extrude(height = height) {
    mold();
};

difference() {
    translate(v = [0,0,height / 2 - h / 2]) {
        linear_extrude(height = height/4) {
            rotate(a = [0, 0, 30]) {
                square(size = [ thickness, 2 * (radius + expand) ], center = true);
            };
            rotate(a = [0, 0, 90]) {
                square(size = [ thickness, 2 * (radius + expand) ], center = true);
            };
        };
        cylinder(h = height / 2 + h , r1 = 8, r2 = 8);
    };
    cylinder(h = height * 2, r1 = 8 - thickness, r2 = 8 - thickness);
};


