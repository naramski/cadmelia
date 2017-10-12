maxR = 10;

w = 30;
h = 30;
d = 30;

for(i = [ 0 : 40 ]) {
    translate(v = [ w*rand(), h*rand(), d*rand() ]) {
        sphere(maxR*rand());
    }
}
