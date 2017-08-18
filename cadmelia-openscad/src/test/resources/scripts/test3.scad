
module flower(height=1, size=4, expand=5) {
    instruction1();
    instruction2();
    instruction3();
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

module_call(height);

module mold() {
    difference() {
        flower(20, radius, expand);
    }
}
