
difference() {

    union() {

        rotate(a = [ 0, 0, 120 * 0 ]) {

            linear_extrude(height = 2)
                polygon(points = [
                    [ -3.5, 0 ],
                    [ 3.5, 0 ],
                    [ -1.75, 35 ]
                ]);

            rotate(a = [ 0, 0, -90 ])
                translate(v = [ 0, 0, -1.25 ])
                    rotate(a = [ -90, 0, 0 ])
                        linear_extrude(height = 2.5)
                            polygon(points = [
                                [ 3.5, 2 ],
                                [ -1, 2 ],
                                [ 3.5, 5 ]
                            ]);

        };

        rotate(a = [ 0, 0, 120 * 1 ]) {

            linear_extrude(height = 2)
                polygon(points = [
                    [ -3.5, 0 ],
                    [ 3.5, 0 ],
                    [ -1.75, 35 ]
                ]);

            rotate(a = [ 0, 0, -90 ])
                translate(v = [ 0, 0, -1.25 ])
                    rotate(a = [ -90, 0, 0 ])
                        linear_extrude(height = 2.5)
                            polygon(points = [
                                [ 3.5, 2 ],
                                [ -1, 2 ],
                                [ 3.5, 5 ]
                            ]);

        };


        rotate(a = [ 0, 0, 120 * 2 ]) {

            linear_extrude(height = 2)
                polygon(points = [
                    [ -3.5, 0 ],
                    [ 3.5, 0 ],
                    [ -1.75, 35 ]
                ]);

            rotate(a = [ 0, 0, -90 ])
                translate(v = [ 0, 0, -1.25 ])
                    rotate(a = [ -90, 0, 0 ])
                        linear_extrude(height = 2.5)
                            polygon(points = [
                                [ 3.5, 2 ],
                                [ -1, 2 ],
                                [ 3.5, 5 ]
                            ]);

        };

    };

    cube(2.5, 2.5, 6, center=true);

};

