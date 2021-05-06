# ncat-lib

This repository contains the transformation algorithms/modules used by the NCAT web tool.

For additional information, contact:
NOAA National Geodetic Survey,
ngs.infocenter@noaa.gov

Visit:
https://geodesy.noaa.gov/NCAT/

## NOAA Open Source Disclaimer

This repository is a scientific product and is not official communication of the National Oceanic and Atmospheric Administration, or the United States Department of Commerce. All NOAA GitHub project code is provided on an ?as is? basis and the user assumes responsibility for its use. Any claims against the Department of Commerce or Department of Commerce bureaus stemming from the use of this GitHub project will be governed by all applicable Federal law. Any reference to specific commercial products, processes, or services by service mark, trademark, manufacturer, or otherwise, does not constitute or imply their endorsement, recommendation or favoring by the Department of Commerce. The Department of Commerce seal and logo, or the seal and logo of a DOC bureau, shall not be used in any manner to imply endorsement of any commercial product or activity by DOC or the United States Government.

Please note that there is no confidentiality on any code submitted through pull requests to NOAA National Geodetic Survey. If the pull requests are accepted and merged into the master branch then they become part of publicly accessible code. 

## License

Software code created by U.S. Government employees is not subject to copyright in the United States (17 U.S.C. �105). The United States/Department of Commerce reserve all rights to seek and obtain copyright protection in countries other than the United States for Software authored in its entirety by the Department of Commerce. To this end, the Department of Commerce hereby grants to Recipient a royalty-free, nonexclusive license to use, copy, and create derivative works of the Software outside of the United States.

## Building NCAT library from source code

Ant tool is required to build a library jar from the source code.
Run ant using the following command:
ant -f buildjar.xml

The command generates jtransform_thin.jar under the "dist" directory
Navigate to dist directory to use the NCAT command line version

## Running NCAT from the Command Line.
No internet connectivity is needed to run NCAT from the command line.
You may run NCAT with or without datum transformation
Nadcon and/or Vertcon grids are needed for datum transformation. Grids are available as a 
separate download on the NCAT site.

To run NCAT from the command line:
Navigate to the directory where the downloaded application is unzipped
Run the command using a format that meets your requirement(see formats and examples below). 

Results are generated in JSON format which may be directed to a text file.
Commands may be embedded in a script or program as needed.

## Reference Frames (historically called “horizontal datum”) Supported for Transformation:

The following are the valid reference frames for use as input or output. Transformation
takes place only when input and output datums are different. If no transformation is possible,
results are returned as N/A
 
 StGeorge:SG1897,SG1952,NAD83(1986)
 StPaul:SP1897,SP1952,NAD83(1986)
 StLawrence:SL1952,NAD83(1986)
 Alaska:NAD27,NAD83(1986),NAD83(1992),NAD83(NSRS2007),NAD83(2011)
 Conus:USSD,NAD27,NAD83(1986),NAD83(HARN),NAD83(FBN),NAD83(NSRS2007),NAD83(2011)
 Hawaii:OHD,NAD83(1986),NAD83(1993),NAD83(PA11)
 PRVI:PR40,NAD83(1986),NAD83(1993),NAD83(1997),NAD83(2002),NAD83(NSRS2007),NAD83(2011)
 AS:AS62,NAD83(1993),NAD83(2002),NAD83(PA11)
 GuamCNMI:GU63,NAD83(1993),NAD83(2002),NAD83(MA11)

## Geopotential Datums (historically called “vertical datum”) Supported for Transformation:

The following are the valid geopotential datums for use as input or output. Transformation
takes place only when input and output datums are different. If no transformation is possible,
results are returned as N/A

Conus:NGVD29,NAVD88
Alaska:NGVD29,NAVD88
Guam:GUVD63,GUVD04
CNMI:LT,NMVD03
VI:LT,VIVD09	
PR:LT,PRVD02	
AS:LT,ASVD02

## Format of latitude and longitude

Latitude and longitude may be entered in decimal degrees, 
Degrees-Minutes-Seconds(DMS), or mixed mode.
If a DMS format is used:
	Prefix the value with a hemisphere designator (N or S for latitudes and E or W for longitudes),
	and use
 	DDMMSS.ssssss format for latitudes and
	DDDMMSS.ssssss format for longitudes
	Decimal seconds are optional, up to 6 decimals may be used.

For decimal degrees, negative west longitude convention is used.

Where applicable, if no ellipsoid height is used, ellipsoid height input should be set to N/A;
if no orthometric height is used, orthometric height input should be set to N/A.

A description of keywords used in formats is as follows.

Keyword		Description
_________       _____________________________________________________________________________________
<lat>       	Latitude (in decimal degrees or DMS)
<lon>       	Longitude (in decimal degrees or DMS)
<h>       	Ellipsoid height in meters. Use the keyword N/A, if no ellipsoid height is available.
	    	For SPC conversion, ellipsoid height must be in the same units as northing and easting.
<H>             Orthometric height in meters
<inDatum>   	Input reference frame (a valid input reference frame or N/A)
<outDatum>  	Output reference frame (a valid output reference frame or N/A)
<inVertDatum>   Input geopotential Datum (a valid input geopotential datum or N/A)
<outVertDatum>  Output Datum (a valid output geopotential datum or N/A)
<spcZone>   	A 4-digit SPC zone
<utmZone>   	A 2-digit UTM zone; For UTM conversion, utmzone is a required field. For other conversions,
		UTM zone is automatically determined when the keyword "auto" used. You may optionally 
		override this by specifying your own 2-digit UTM zone
<destZone>  	An optional 4-digit SPC zone or 2-digit UTM zone used to convert SPC or UTM coordinate
	 	from one zone to the other
<northing>  	Northing 
<easting>   	Easting  
<units>     	Units of northing and easting (m,usft,or ift); needed only for SPC conversions 
<hemi>      	Hemisphere for UTM conversion (N or S); needed only for UTM conversions 
<usng>      	USNG at 1-meter resolution
<x>         	X-coordinate in meters
<y>         	Y-coordinate in meters
<z>         	Z-coordinate in meters
<radius>    	Equatorial radius in meters
<invf>      	Inverse of flattening
<grids dir> 	Directory of Nadcon5 or Vertcon3 grids (unzipped directory)
llh         	lat-lon-ellipsoid height is being used for input, case sensitive
llH         	lat-lon-orthometric height is being used for input, case sensitive
spch         	SPC-ellipsoid coordinate is being used for input, case sensitive
spcH         	SPC-orthometric coordinate is being used for input, case sensitive
utmh         	UTM-ellipsoid height coordinate is being used for input, case sensitive
utmH         	UTM-orthometric height coordinate is being used for input, case sensitive
usngh        	USNG-ellipsoid height coordinate is being used for input, case sensitive
usngH        	USNG-orthometric height coordinate is being used for input, case sensitive
XYZ         	XYZ coordinate is being used for input, case sensitive

-Dparms     	A keyword used on the command line to provide input data
-Dgpath     	A keyword used to specify the path of grids directory

## Conversion without Transformation

When reference frames or datums chosen for input and output are the same, no transformation
takes place. No grids are needed for this option.

Command line formats and examples:
         

Format#1 (lat-long-ellipsoid height conversion):
java -Dparms=llh,<lat>,<lon>,<h>,<inDatum>,<outDatum><spcZone><utmZone> -jar jtransform_thin.jar

example#1:
java -Dparms=llh,40.0,-80.0,100.0,NAD83(1986),NAD83(1986),3702,auto -jar jtransform_thin.jar

Format#1a (lat-long-ellipsoid height conversion for an international coordinate)
java -Dparms=llh,<lat>,<lon>,<h>,<radius>,<invf>,<utmZone> -jar jtransform_thin.jar

example#1a:
java -Dparms=llh,-33.8688,151.2093,100.0,6378160.0,298.25,auto -jar jtransform_thin.jar

Format#1b (lat-long-orthometric height conversion)
java -Dparms=llH,<lat>,<lon>,<H>,<inDatum>,<outDatum><spcZone><utmZone> <inVertDatum> <outVertDatum -jar jtransform_thin.jar

example#1b:
java -Dparms=llH,40.0,-80.0,100.0,NAD83(2011),NAD83(2011),3702,auto,NAVD88,NAVD88 -jar jtransform_thin.jar

Format#2 (SPC-ellipsoid height conversion):
java -Dparms=spch,<spcZone>,<northing>,<easting>,<units>,<inDatum> <outDatum><utmZone><h> -jar jtransform_thin.jar

example#2:
java -Dparms=spch,2402,173099.419,503626.812,m,NAD83(2011),NAD83(2011),auto,100.0 -jar jtransform_thin.jar

Format#2a (SPC-ellipsoid height conversion; use this format to convert SPC from one zone to the other):
java -Dparms=spch,<spcZone>,<northing>,<easting>,<units>,<inDatum> <outDatum><utmZone><h><destZone> -jar jtransform_thin.jar

example#2a:
java -Dparms=spch,2402,173099.419,503626.812,m,NAD83(2011),NAD83(2011),auto,100.0,2401 -jar jtransform_thin.jar

Format#2b (SPC-orthometric height conversion):
java -Dparms=spcH,<spcZone>,<northing>,<easting>,<units>,<inDatum> <outDatum><utmZone><H><inVertDatum> <outVertDatum> -jar jtransform_thin.jar

example#2b:
java -Dparms=spcH,2402,173099.419,503626.812,m,NAD83(2011),NAD83(2011),auto,100.0,NAVD88,NAVD88 -jar jtransform_thin.jar

Format#2c (SPC-orthometric height conversion; use this format to convert SPC from one zone to the other):
java -Dparms=spcH,<spcZone>,<northing>,<easting>,<units>,<inDatum> <outDatum><utmZone><H><inVertDatum> <outVertDatum><destZone> -jar jtransform_thin.jar

example#2c:
java -Dparms=spcH,2402,173099.419,503626.812,m,NAD83(2011),NAD83(2011),auto,100.0,NAVD88,NAVD88,2401 -jar jtransform_thin.jar

Format#3 (UTM-ellipsoid height conversion)
java -Dparms=utmh,<utmZone>,<northing>,<easting>,<hemi>,<inDatum>,<outDatum>,<spcZone><h> -jar jtransform_thin.jar

Example#3
java -Dparms=utmh,15,4138641.144,547883.655,N,NAD83(2011),NAD83(2011),2402,100.0 -jar jtransform_thin.jar

Format#3a (UTM-ellipsoid height conversion; use this format to convert UTM from one zone to the other):
java -Dparms=utmh,<utmZone>,<northing>,<easting>,<hemi>,<inDatum>,<outDatum>,<spcZone><h><destZone> -jar jtransform_thin.jar

Example#3a
java -Dparms=utmh,15,4138641.144,547883.655,N,NAD83(2011),NAD83(2011),2402,100.0,14 -jar jtransform_thin.jar

Format#3b (UTM-orthometric height conversion)
java -Dparms=utmH,<utmZone>,<northing>,<easting>,<hemi>,<inDatum>,<outDatum>,<spcZone><H><inVertDatum><outVertDatum> -jar jtransform_thin.jar

Example#3b
java -Dparms=utmH,15,4138641.144,547883.655,N,NAD83(2011),NAD83(2011),2402,100.0,NAVD88,NAVD88 -jar jtransform_thin.jar

Format#3c (UTM-orthometric height conversion; use this format to convert UTM from one zone to the other):
java -Dparms=utmH,<utmZone>,<northing>,<easting>,<hemi>,<inDatum>,<outDatum>,<spcZone><H><inVertDatum><outVertDatum><destZone> -jar jtransform_thin.jar

Example#3c
java -Dparms=utmH,15,4138641.144,547883.655,N,NAD83(2011),NAD83(2011),2402,100.0,NAVD88,NAVD88,14 -jar jtransform_thin.jar

Format#3d (UTM-ellipsoid height conversion; use this format for international coordinates)
java -Dparms=utmh,<utmZone>,<northing>,<easting>,<hemi>,<radius> <invf><eht> -jar jtransform_thin.jar

Example#3d
java -Dparms=utmh,56,6250935.338,334368.032,S,6378160.0,298.25,100.0 -jar jtransform_thin.jar

Format#4 (USNG-ellipsoid height conversion)
java -Dparms=usngh,<usng>,<inDatum>,<outDatum>,<spcZone><h> -jar jtransform_thin.jar

Example#4
java -Dparms=usngh,15SWB4788338641,nad83(2011),nad83(2011),2402,100.0 -jar jtransform_thin.jar

Format#4a (USNG-orthometric height conversion)
java -Dparms=usngH,<usng>,<inDatum>,<outDatum>,<spcZone><H><inVertDatum><outVertDatum> -jar jtransform_thin.jar

Example#4a
java -Dparms=usngH,15SWB4788338641,nad83(2011),nad83(2011),2402,100.0,NAVD88,NAVD88 -jar jtransform_thin.jar


Command format#4b (USNG-ellipsoid height; use this format for an international coordinate)
java -Dparms=usngh,<usng>,<radius>,<invf><h> -jar jtransform_thin.jar

Example#4b
java -Dparms=usngh,56HLH3436850935,6378160.0,298.25,100.0 -jar jtransform_thin.jar

Format#5 (XYZ conversion)
java -Dparms=xyz,<x>,<y>,<z>,<inDatum>,<outDatum>,<spcZone>,<utmZone>

Example#5
java -Dparms=xyz,-217683.881,-5068933.259,3852162.058,NAD83(2011),NAD83(2011),2402,auto -jar jtransform_thin.jar

Command format#5a (XYZ conversion for an international coordinate)
java -Dparms=xyz,<x>,<y>,<z>,<radius>,<invf>,><utmZone>

Example#5a
java -Dparms=xyz,-4646068.143,2553215.614,-3534384.646,6378160.0,298.25,auto -jar jtransform_thin.jar

Sample output:
{
  "ID": "1570121042377",
  "nadconVersion": "5.0",
  "vertconVersion": "3.0",
  "srcDatum": "NAD83(1986)",
  "destDatum": "NAD83(1986)",
  "srcVertDatum": "N/A",
  "destVertDatum": "N/A",
  "srcLat": "40.0000000000",
  "srcLatDms": "N400000.00000",
  "destLat": "40.0000000000",
  "destLatDms": "N400000.00000",
  "sigLat": "0.000000",
  "srcLon": "-80.0000000000",
  "srcLonDms": "W0800000.00000",
  "destLon": "-80.0000000000",
  "destLonDms": "W0800000.00000",
  "sigLon": "0.000000",
  "srcEht": "100.000",
  "destEht": "100.000",
  "sigEht": "0.000",
  "srcOrthoht": "N/A",
  "destOrthoht": "N/A",
  "sigOrthoht": "N/A",
  "spcZone": "PA S-3702",
  "spcNorthing_m": "76,470.584",
  "spcEasting_m": "407,886.482",
  "spcNorthing_usft": "250,887.243",
  "spcEasting_usft": "1,338,207.567",
  "spcNorthing_ift": "250,887.744",
  "spcEasting_ift": "1,338,210.244",
  "spcConvergence": "-01 27 35.22",
  "spcScaleFactor": "0.99999024",
  "spcCombinedFactor": "0.99997455",
  "utmZone": "UTM Zone 17",
  "utmNorthing": "4,428,236.065",
  "utmEasting": "585,360.462",
  "utmConvergence": "00 38 34.17",
  "utmScaleFactor": "0.99968970",
  "utmCombinedFactor": "0.99967402",
  "x": "849,623.061",
  "y": "-4,818,451.818",
  "z": "4,078,049.851",
  "usng": "17TNE8536028236"
}

## Conversion with Datum Transformation

Transformation grids are needed for this option. See
above for a list of datums available for a region of interest. 

Download Nadcon5 and/or Vertcon3 grids from the NCAT site,
if not done already.

Unzip downloaded grids to a directory of choice. Please note that both Nadcon5 and
Vertcon3 grids must be together in one directory with no subdirectories separating
them.

Add "-Dgpath=<grids dir>" to a command of interest given above. 

Assuming the grids are unzipped to "/grids" directory, the following examples
perform transformations for both reference frame and geopotential datum and return the
coordinate set in output reference frame and geopotential datum. 
As before, the command must be run from a directory where the application is unzipped.

java -Dgpath=/grids -Dparms=llH,40.0,-80.0,100.0,NAD83(2011),NAD83(NSRS2007),3702,auto,NGVD29,NAVD88 -jar jtransform_thin.jar 
java -Dgpath=/grids -Dparms=spcH,2402,173099.419,503626.812,m,NAD83(2011),NAD83(NSRS2007),auto,100.0,NGVD29,NAVD88 -jar jtransform_thin.jar 
java -Dgapth=/grids -Dparms=utmH,15,4138641.144,547883.655,N,NAD83(2011),NAD83(NSRS2007),2402,100.0,NGVD29,NAVD88,14 -jar jtransform_thin.jar 
java -Dgapth=/grids -Dparms=usngH,15SWB4788338641,nad83(2011),nad83(NSRS2007),2402,100.0,NGVD29,NAVD88 -jar jtransform_thin.jar 
java -Dparms=xyz,-217683.881,-5068933.259,3852162.058,NAD83(2011),NAD83(NSRS2007),2402,auto -jar jtransform_thin.jar 

## Using NCAT in a java program

The class CLDriver located in the package gov.noaa.ngs.transform.test provides sample conversions and trasformations.
Additionally, the docs folder in the package has companion javadoc which may be used for custom output formats, conversions and/or transformations.

## Sample use cases and input/output files

You may use sample input and output files for validation to ensure all conversions and transformations are working as expected.
Here are the steps:

1. Navigate to the dist directory where the library jar is located.

2. To validate conversions, run the following command:
java -cp jtransform_thin.jar gov.noaa.ngs.transform.test.TestDriver

The command should generate the following output:

Performing conversions against testdata.......
All Tests Passed. Actual results match expected results

3. To validate nadcon5 transformations, run the following command (please note that the test data contains a few invalid use cases):
java -cp jtransform_thin.jar gov.noaa.ngs.nadcon5.test.TestDriver

After a few error messages pertaining to invalid use cases, the command should generate the following output:
All Nadcon Tests Passed. Actual results match expected results

4. To validate vertcon3 transformations run the following command:
java -cp jtransform_thin.jar gov.noaa.ngs.vertcon.test.TestDriver

The command should generate the following output:

Running tests for PR
All Tests Passed
Running tests for AS
All Tests Passed
Running tests for VI
All Tests Passed
Running tests for ALASKA
All Tests Passed
Running tests for CONUS
All Tests Passed
Running tests for CNMI
All Tests Passed
Running tests for GUAM
All Tests Passed

