# Execution and manual

## Project execution:

The execution is done through MATLAB function in attributeFiltering.m file. It is necessary to initialize dip_image toolbox by providing the path to dipstart.m file, usually stored in Program Files:
	
	run(C:\Program Files\dip\dipstart.m) 

Then the AttributeFiltering.jar file needs to be placed on the java class path in MATLAB by javaaddpath command that takes in its argument the full path to the file. This file is originaly located in AttributeFiltering\dist folder. If the current directory in MATLAB is

	C:\Documents\MATLAB\Attribute Filtering,

containing attributeFiltering.m file and AttributeFiltering.jar, then the following code adds the jar file to java class path:

	javaaddpath(’C:\Documents\MATLAB\Attribute Filtering\AttributeFiltering.jar’)

After that, it is possible to call attributeFiltering.m function (it must be in the current directory in MATLAB).


## For attribute filtering using max-tree:

*img_out = attributeFiltering(img_in, attribute, threshold, filterType, k)*

*img_in*: dip_image, cannot contain floating point intensities
*attribute*: 
* 'area' - number of pixels in the component
* 'height' - maximal value in the component minus the value of the canonical node
* 'value' - intensity of the node in the tree
* 'perimeter'- number of "edges" surrounding the component
* 'eccentricity'
* 'elongation'- using bounding box
* 'simplicity'- area/perimeter
* 'compactness'- perimeter^2 / (4*PI*area)
* 'widthBB' - width of the bounding box
* 'heightBB' - height of the bounding box

*threshold*:
	threshold for filtering - either one number or an array [t1, t2] specifying lower and upper threshold.
	If only one number is used - components having the value above threshold are kept. 
	If two number are used - components having the value above first and below the second one are kept.

*filter*:
* 'min' - minimum filtering rule
* 'max' - maximum filtering rule
* 'direct'
* 'subtractive'
* 'Viterbi' - counts only the number of changes that have to be made to make a set of decisions increasing
* 'weightedViterbi' - counts the weight of the changed decisions
* 'kSubtractive' - for hyperconnected attribute filters

*k*:
	number that is specified only if the filtering rule is 'kSubtractive'


## For attribute filtering using dual-input max-tree:

*img_out = attributeFiltering(newimar(image, mask), attribute, threshold, filterType, k)*

image and mask are both greyscale dip_image with the same size, no ordering between them is necessary
