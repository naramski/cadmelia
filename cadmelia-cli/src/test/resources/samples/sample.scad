cube(2, center=true);

translate(v = [3, 0, 0]) {
    sphere(1.25);
};

translate(v = [6, 0, 0]) {
    cylinder(h = 3, center=true);
};

translate(v = [9, 0, 0]) union() {
    sphere(1.25);
    cube(2, center=true);
};

translate(v = [12, 0, 0]) difference() {
    cube(2, center=true);
    sphere(1.25);
};

translate(v = [15, 0, 0]) intersection() {
    cube(2, center=true);
    sphere(1.25);
};

translate(v = [18, 0, 0]) difference() {
    intersection() {
        cube(2, center=true);
        sphere(1.25);
    };
    rotate(a = [90,0,0]) {
        cylinder(h = 3, center=true);
    };
    rotate(a = [0,90,0]) {
        cylinder(h = 3, center=true);
    };
    cylinder(h = 3, center=true);
};
