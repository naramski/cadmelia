radius = 1.2;
spacing = 0.25;

difference() {

    cube(size = [ 30, 30, 1 ], center=true);

    for(y = [0 : 10]) {

        for(x = [0 : 10]) {

            translate(v = [ (x - 5) * (radius * 2 + spacing), (y - 5) * (radius * 2 + spacing), 0.0 ])
                sphere(r = radius);

        }

    }

}

