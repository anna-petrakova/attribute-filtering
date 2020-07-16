function [img_out] = attributeFiltering(input_img, attribute, threshold, filterType, k)
% function [img_out] = attributeFiltering(img_in, attribute, threshold, filterType, k)
% Input parameters:
%	   img_in  - image of type DipImage for max-tree, or array of 
%                images created as newimar(img, mask) for dual-input 
%                 max-tree
%	attribute  - name of attribute in apostrophes, 
%                'area', 'height', 'perimeter', 'eccentricity', 'elongation', 
%                'compactness', 'simplicity', 'value' 'widthBB', 'heightBB'
%	threshold  - a number t, or an array of two numbers [t1, t2]
%	filterType - name of filtering rule, 
%                'min', 'max', 'direct', 'subtractive', 
%                'viterbi', 'weightedViterbi', 'kSubtractive'
%	        k  - number required only for k-subtractive rule
% Output parameters:
%	  img_out  - filtered image of type DipImage
% Example of usage:
%     output = attributeFiltering(img, 'area', 500, 'direct')
%     output = attributeFiltering(newimar(img,mask), 'eccentricity', [2,5], 'kSubtractive', 10)


if strcmp(filterType, 'kSubtractive') && nargin ~= 5
    error('When using kSubtractive filtering, the argument k must be specified.')
end

if ~strcmp(filterType, 'kSubtractive') && nargin ~= 4
    error('Function requires exactly 4 arguments.')
end

s = imarsize(input_img);
% original image is compulsory
img_in = input_img{1};

img_matlab = dip_array(img_in);
img_matlab2 = img_matlab'; % switch rows and columns
img = int32((img_matlab2(:))'); % convert into row vector
im = attributefiltering.Image2D(int16(size(img_in, 1)), int16(size(img_in, 2)), img);

if s(1) > 1 % mask image was provided in the input_img, array has two columns
   mask_dip = input_img{2};
   mask_matlab = dip_array(mask_dip);   
   mask_matlab2 = mask_matlab'; % switch rows and columns
   m = int32(mask_matlab2(:))';
   mask = attributefiltering.Image2D(int16(size(img_in, 1)), int16(size(img_in, 2)), m); % mask and img_in are of same size
else % original image is considered to be the mask
   mask = im; 
end

conn = connectivity.Connectivity8(int16(size(img_in, 1)), int16(size(img_in, 2)));

if s(1) > 1
   tree = attributefiltering.DualInputMaxTree(im, mask, conn);
else
   tree = attributefiltering.MaxTreeBerger(im, conn);
end
tree.constructTree();
% tree.getCanonicalCount() %- uncomment to see how many canonical nodes (components) the tree has
compute = computingattributes.ComputeAttribute(tree.getTree(), mask, conn);

switch(attribute)
    case 'area'
        attr = computingattributes.Area();
    case 'perimeter'
        attr = computingattributes.Perimeter();
    case 'eccentricity'
        attr = computingattributes.Eccentricity();
    case 'compactness'
        attr = computingattributes.Compactness();
    case 'elongation'
        attr = computingattributes.Elongation();
    case 'height'
        attr = computingattributes.Height();
    case 'simplicity'
        attr = computingattributes.Simplicity();
    case 'value'
        attr = computingattributes.Value();
    case 'widthBB'
        attr = computingattributes.WidthBB();
    case 'heightBB'
        attr = computingattributes.HeightBB();
    otherwise
        error('Unexpected attribute type. No filter used.')
end
compute.compute(attr);

if size(threshold,2) == 2
   upperThreshold = threshold(2);
   lowerThreshold = threshold(1);
else
   upperThreshold = Inf;
   lowerThreshold = threshold(1);
end

if ~isnumeric(upperThreshold)
   error('Threshold must be a number.') 
end

if ~isnumeric(lowerThreshold)
   error('Threshold must be a number.') 
end

filter = filtering.Filtering(tree.getTree(), lowerThreshold, upperThreshold);
switch (filterType) 
            case 'min'
                filter.minFiltering();
            case 'max'
                filter.maxFiltering(tree.getCanonicalCount());
            case 'viterbi'
                filter.viterbiFiltering(tree.getCanonicalCount());
            case 'weightedViterbi'
                filter.weightedViterbiFiltering(tree.getCanonicalCount());
            case 'subtractive'
                filter.subtractiveFiltering();
            case 'kSubtractive'
                filter.kSubtractiveFiltering(k, tree.getCanonicalCount());
            case 'direct'
                filter.directFiltering();
            otherwise
                error('Unexpected filtering type. No filter used.')
end
tree.canonicalize();
output_image = tree.reconstructImage();

rows = size(img_in, 2);
columns = size(img_in, 1);
img_out_matlab = reshape((output_image.getData())', [columns rows]);
img_out_final = img_out_matlab';
img_out = dip_image(img_out_final);

