
//Start with Cup Dimensions - these are for a Starbucks $1 Reusable Cup
bottom_diameter=59.5;
top_diameter=82;
//Make sure the height is measured as the distance between your top and bottom radius measurements!
height=135;

//Now some basic properties of your sleeve
color="aqua"; //[black,silver,gray,white,maroon,red,purple,fuchsia,green,lime,olive,yellow,navy,blue,teal,aqua,brown,sienna]
thickness=2; //[1:4]
border=3; //[1:30]
sleeve_height_percent=95;//[10:95]
//Use this to move the sleeve up or down on the cup
sleeve_z_offset=0;

//And finally the pattern variables...
line_thickness=4;
sides=6;//[3:20]
size=18;//[1:20]
overlap=5;//[0:10]
//This is the spin of each shape about its center
spin=22.5;//[-90:90]
//This is the amount the pattern is made to "spiral" around the cup, vs. stack straight up
pattern_spin=9;//[0:10]

part=1;//[1:Render,2:Final]

////////////////////////////////

bottom_radius=bottom_diameter/2;
top_radius=top_diameter/2;
rb=bottom_diameter/2+thickness;
rt=top_diameter/2+thickness;
h=height;
lt=line_thickness;
sh=sleeve_height_percent/100*h;
szo=sleeve_z_offset;

res=30;


theta=atan((rt-rb)/h);
count=24-size;
thetac=360/count;
rpb=rb*3.14/count;
rp=rpb*(1+overlap*.2);

rows=round(h/(2*rpb));
echo("val");
echo(h);
echo(sleeve_height_percent/100*h);
echo(sh);

echo(szo+(h-sh)/2);
translate([0,0,szo+(h-sh)/2])cylinder(r=rt*2,h=border,$fn=res);

echo(h-border+szo-(h-sh)/2);
translate([0,0,h-border+szo-(h-sh)/2])cylinder(r=rt*2,h=border,$fn=res);
