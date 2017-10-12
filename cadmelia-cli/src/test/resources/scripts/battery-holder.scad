
mountingThickness = 3.0;
boardToBoardSpacing = 30.0;
connectorDepth = 25;
pegHeight = 1;
pegToothHeight = 0.3;
pegOverlap = 0.6;
boardMountingWidth = 8.11;
batteryHeight = 22;
batteryLength = 54;
footHeight = 25;
footSize = 10;

th = 3;
smh = boardMountingWidth;
mth = mountingThickness;

pth = pegToothHeight;
ph = pegHeight;
po = pegOverlap;

o = 13;

linear_extrude(height = connectorDepth) {

    polygon(points = [
        [ -th, -th ],
        [ smh + pth + ph+o, -th ],
        [ smh + pth + max(ph / 3, 0.4)+o, 0 + po ],
        [ smh + pth+o, 0 + po ],
        [ smh+o, 0 ],
        [ 0+o, 0 ],
        [ 0+o, mth ],
        [ smh+o, mth ],
        [ smh+o, mth + th ],
        [ 0, mth + th ],
        [ 0, mth + th + batteryHeight ],
        [ batteryLength, mth + th + batteryHeight ],
        [ batteryLength, mth + th + batteryHeight * 0.3 ],
        [ batteryLength + th, mth + th + batteryHeight * 0.3 ],
        [ batteryLength + th, mth + th + batteryHeight + th ],
        [ 0, mth + th + batteryHeight + th ],
        [ 0, mth + th + batteryHeight + th + footHeight - th * 2 ],
        [ footSize, mth + th + batteryHeight + th + footHeight - th ],
        [ footSize, mth + th + batteryHeight + th + footHeight ],
        [ -th, mth + th + batteryHeight + th + footHeight ] ]);

};
