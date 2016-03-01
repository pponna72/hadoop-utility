# MRDynamicSplit
Utility to dynamically determine MR split size

----------------------
Introduction
----------------------
Dynamic Split Size utility is used to determine the MR split size dynamically by taking file size as input. Also the maximum allowed number of splits and maximum size allowed for split is also provided as input. Threshold is set not to allow number of splits and split size to exceed drastically beyond control.

----------------------
Modules
----------------------
The utility has the functionality that can be used to determine split size dynamically based on file size. 

----------------------
Requirement
----------------------
The following runtime requirements are required for running the utility.
-	CDH 5.1 or above
-	JRE 1.7 or above

--------------------------
Invoking the functionality
--------------------------
Utility can be used to invoke the functionality to achieve the purpose
-	MRDynamicSplit dynamicSplit = new MRDynamicSplit();
-	dynamicSplit.calculateSplitSize(conf, fileSizeInBytes, maxNoOfSplits, maxSplitSize);

maxNoOfSplits and maxSplitSize are threshold to determine the split size such that number of splits and split size do not exceed the threshold in calculating the split size.

----------------------
Utility Output
----------------------
calculateSplitSize() will return the split size based on the file size considering the threshold on number of splits and split size.


