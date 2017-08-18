
// data taken from
// https://www.sparkfun.com/datasheets/Prototyping/microSD_Spec.pdf
// total card width

A = 10.9;
A1 = 9.6;

A8 = 0.6;

B = 14.9;

B1 = 6.3;

B10 = 7.8;

B11 = 1.1;

C1 = 0.6;

A_ = A - A1;

B_ = B - B1 + A_;

linear_extrude(height = C1) {

    polygon(points = [
        [ 0, 0 ],
        [ A, 0 ],
        [ A, B ],
        [ A_, B ],
        [ A_, B_ ],
        [ 0, B - B1 ],
        [ 0, B - B10 ],
        [ A8, B - B10 ],
        [ A8, B - B10 - B11 ],
        [ 0, B - B10 - B11 - A8 ] ]);

};

