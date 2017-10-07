count = 11;
space = 10;
size = 3;
height = (count - 1) * space + size;

module bars2() {
    union() {
        for(i=[0:count-1]) {
            for(j=[0:count-1]) {
                translate([i*space,j*space,0])
                    cube([size,size,height]);
            }
        }
        for(i=[0:count-1]) {
            for(j=[0:count-1]) {
                translate([0,i*space,j*space])
                    cube([height,size,size]);
            }
        }
        for(i=[0:count-1]) {
            for(j=[0:count-1]) {
                translate([i*space,0,j*space])
                    cube([size,height,size]);
            }
        }
    }
}

module total() {
    difference() {
        intersection() {
            translate([-height/2, -height/2, -height/2])
                bars2();
            sphere(height/2);
        }
        sphere(height/2*.6);
        text1();
        text2();
        text3();
    }
}

module text1() {
    rotate([90,0,-30])
    translate([0,0,-100])
        linear_extrude(height=200, scale = .5)
            scale([2,2,1])
                text("Solution", halign="center", valign="center");
}

module text2() {
    translate([0,0,10])
    rotate([90,10,75])
    translate([0,0,-100])
        linear_extrude(height=200, scale = .7)
            scale([2,2,1])
                text("Problem", halign="center", valign="center");
}

module text3() {
    translate([0,0,10])
    rotate([50,10,40])
    translate([0,0,-100])
        linear_extrude(height=200, scale = .7)
            scale([2,2,1])
                text("Error", halign="center", valign="center");
}

total();
