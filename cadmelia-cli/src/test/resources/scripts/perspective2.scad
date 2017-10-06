
module text1() {
    rotate([90,0,-30])
        translate([0,0,-100])
        cube([20,5,200]);
}

module text2() {
    translate([0,0,10])
    rotate([90,10,75])
        translate([0,0,-100])
        cube([20,5,200]);
}

module text3() {
    translate([0,0,10])
    rotate([50,10,40])
        translate([0,0,-100])
        cube([20,5,200]);
}

text1();
text2();
//text3();
