# transformDEMdata

*This project is a component of (https://github.com/larvinloy/docker-chiminey)*

This package allows the user to create input files from ASCII Digital Elevation Model files and jpeg/png images. 
It creates a matrix as shown in (https://github.com/chiminey/chiminey/blob/3drac_chiminey/chiminey/3drac/README.md). In case of
images, it creates a matrix of RGB values.

1. To convert DEM, run the DEMToInput.java. It expects two arguments, name of the dem file and a name for the output file.

2. To convert JPEG/PNG, run the ImageToRGBInput.java. It expects two arguments, name of the dem file and a name for the output file.
