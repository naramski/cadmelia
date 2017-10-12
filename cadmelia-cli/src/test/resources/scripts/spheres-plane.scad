radius = 1.2;
spacing = 0.25;

for(y = [0 : 10]) {

    for(x = [0 : 10]) {

        translate(v = [ x  * (radius * 2 + spacing), y * (radius * 2 + spacing), 0.0 ])
            sphere(r = radius);

    };

};

