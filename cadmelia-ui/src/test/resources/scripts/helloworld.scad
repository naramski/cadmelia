
difference() {

    cube([85,20,10]);

    translate([15,14,-10])
        linear_extrude(40)
            text("Hello World !");

}

sphere(5);

translate([85, 20, 10]) sphere (7);

translate([0, 20, 10]) sphere (3);