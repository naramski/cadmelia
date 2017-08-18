circle(1);
circle(size);
circle(size=3);
circle(size,size);
circle( 1);
circle( 1 );
circle( size);
circle( size) ;
circle(size = 3);
rotate(1) circle(1,2);
circle(4,5,2);
circle(4,5 ,2);
circle(4, 5,2);
circle(4,5, 2) ;
circle();
circle(4,55, 2);
cube (x,xy, z);
circle(4 , 5,2);
// COMMENT
rotate([0,0,60]) circle();
rotate([0, 0, 60]) circle();
translate([expand,0,0]) circle();

circle(expand, expand);

translate([expand,0,0]) circle(size, size);
rotate([0, 0, 60]) translate([expand,0,0]) circle(size, size);

rotate([0, 0, 120])
    translate([expand,0,0])
        circle(size, size);

rotate([0, 0, 180])
  translate([expand,0,0]) circle(size, size);

rotate([0, 0, 240]) translate([expand,0,0]) circle(size, size);
rotate([0, 0, 300]) translate([expand,0,0]) circle(size, size);

union() {
    circle(size, size);
    circle(size, size);
}

rotate([0, 0, 180]) test();

circle(4 , 5,222);
