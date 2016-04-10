import numpy as np
import cv2
import math
import random
from matplotlib import pyplot as plt
from matplotlib import cm as cm
from sys import argv

def main():
	minMotifLength= 2	
	maxMotifLength= 7
	motifLengthModifier = 1
	
	patternList = []
	groupIndex = [" " for _ in range (26)]
	letters = ["A", "B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]
	colors = ["r","y","g","b","c","m","k","w","r","y","g"]

	#get all datas and frame numbers from BVH files
	data1,fileLines1 = openFile("01_01.bvh.txt")
	data2,fileLines2 = openFile("01_02.bvh.txt")
	data3,fileLines3 = openFile("02_04.bvh.txt")
	data4,fileLines4 = openFile("07_01.bvh.txt")
	data5,fileLines5 = openFile("09_01.bvh.txt")
	
	#get two points from points.txt
	startPointString,endPointString	= openPointFile("points.txt")
	
	#add up all frame numbers to get the K value
	fileLines = fileLines1 + fileLines2 + fileLines3 + fileLines4 + fileLines5
	K = int(math.log(fileLines,2))	
	
	#stack all data that ready for K-mean clustering
	data = np.vstack((data1,data2,data3,data4,data5))
	data = np.float32(data)
	
	#create a dataList for get all the tokens of each file
	data_1 = np.float32(data1)
	data_2 = np.float32(data2)
	data_3 = np.float32(data3)
	data_4 = np.float32(data4)
	data_5 = np.float32(data5)
	dataList = [data_1,data_2,data_3,data_4,data_5]

	#save old data for comparasion purposes
	dataList_old = [data1,data2,data3,data4,data5]

	# define criteria and apply kmeans()
	criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
	ret,label,center=cv2.kmeans(data,K,criteria,100,cv2.KMEANS_RANDOM_CENTERS)
	
	# Now separate the data, Note the flatten()
	for i in range(K):
		groupIndex[i] = data[label.ravel()== i]
		plt.scatter(groupIndex[i][:,0],groupIndex[i][:,1],c=colors[i])#colors[int(random.randrange(0, 7, 2))])
		plt.scatter(center[i][0],center[i][1],s = 80,c = "y", marker = 's')
	plt.xlabel('Height'),plt.ylabel('Weight')
	plt.show()
	
	#get all radius of each point cloud(group)	
	radiusOfPointCloud = getMaxRadius(center,groupIndex,K)
	
	#use radius of point cloud to detect adjancency
	adjMatrix = getAdjMaxtrix(center, radiusOfPointCloud,K)	
	
	#generate initial motif path based on the K-mean Clustering
	path = getMotifPathInFile(data ,groupIndex,K,letters)
	
	#find pattern in the motif path
	findPattern(path,patternList)
	
	#simplify the patternList
	simplePatternList = simplifyPatternList(patternList)
	
	#join motif path to a motion space string
	motionSpace = "".join(path)
	
	#join each pattern to String
	patternStringList = getPatternStringList(simplePatternList)
	
	#get partition based on the pattern
	partition = getPartition(patternStringList,motionSpace,path)
	
	#get start point and end point
	startPoint = [round(float(x),3) for x in startPointString.split(" ")]
	endPoint = [round(float(x),3) for x in endPointString.split(" ")]
	
	#determine the start group and end group
	startGroup = getGroupByPoint(startPoint,center, radiusOfPointCloud,K,letters)
	endGroup   = getGroupByPoint(endPoint,center, radiusOfPointCloud,K,letters)
	
	#generate a graph dictionary using adjancent matrix
	graph = getGraph(adjMatrix,K,letters)
	
	#ues BFS to find all the path in the graph
	path_queue = MyQUEUE()
	validPath = BFS(graph,startGroup,endGroup,path_queue)

	#get the common letter from motif 		
	letterList = getCommonLetterFromMotif(validPath)

	#get the most fequent char of motif
	mostFeqChar = getMostFeqLetter(motionSpace,letterList)

	#get all substrings from all the motifs
        subStringList,subStringCountList = getSubStringList(validPath,mostFeqChar,minMotifLength,maxMotifLength)

	#get separeted substring for each motif	
	subStrings = getSubString(subStringList,subStringCountList)

	#calculate motif width for each motif
	motifWidth = getMotifWidth(subStrings,groupIndex,K,letters)

	#calculate motif value for each motif
	motifValueList = getMotifValueList(subStrings,motifWidth,motifLengthModifier)

	#get the best motif from the motif list
	bestMotif = getBestMotif(motifValueList,validPath,minMotifLength,maxMotifLength)\

	#get all group tokens for each data file
	groupTokens = getGroupTokens(K,letters,groupIndex,dataList)

	#get existed path from data file and the file
	existPathInFile,tokens = findPatternFromToken(groupTokens,subStrings,bestMotif,validPath)

	#get the left groups
	leftedPath = getLeftedPath(bestMotif,existPathInFile)

	#get the left tokens
	leftedTokens = findGroupTokenUsingPath(leftedPath,groupTokens)	
	
	#get the range of selected frame in the oringinal data for each group
	outputData,rangeOfGroup,nameOfGroup = findPointsFromFile(existPathInFile,tokens,leftedPath,leftedTokens,bestMotif,groupTokens,groupIndex,letters,dataList_old)

	#save the output data to file
	saveToFile(outputData)

	#get the frames from oringinal BVH files use the range of the selected groups
	newBVH = getFramesForBVH(rangeOfGroup,nameOfGroup,letters)

	#generate a new.bvh 
	saveBVHToFile(newBVH,'new.bvh')
	
	print "path: ",path
	print "motion space: " + motionSpace
	print "patter string list: ",patternStringList
	print "partition: ",partition
	print "start group: " + startGroup + " end group: " + endGroup
	print "graph: ",graph
	print "motif: ",validPath
	print "letter list: ",letterList
	print "most feq char: " + mostFeqChar
	print "subString List:" , subStringList
	print "subString List by motifs: ", subStrings
	print "motif Width: ", motifWidth
	print "motif Value List: ", motifValueList
	print "best motif:",bestMotif
	print "group tokens: ",groupTokens
	print "exist path In File and tokens: ",existPathInFile, tokens
	print "lefted path In File and tokens: ",leftedPath,leftedTokens
	print "range Of Group: ",rangeOfGroup
	print "name Of Group: ",nameOfGroup
	
def openFile(fileName):
#open data file with fileName
	data = []
	fileLines = 0
	
	with open(fileName) as f:
		lines = f.read().splitlines()
		
	for line in lines:
		fileLines += 1
		floats = [float(x) for x in line.split(" ")]
		data.append(floats)
		
	return data, fileLines

def openPointFile(fileName):
#open point file with fileName
	data = []
	
	with open(fileName) as f:
		lines = f.read().splitlines()
		
	for line in lines:
		str = [x for x in line.split(" ")]
		data.append(str)	
		
	startPointString = data[0][0] + " " + data[0][1] 
	endPointString = data[1][0] + " " + data[1][1] 
	
	return startPointString,endPointString
	
def openBVHFile(fileName):
#open BVH file with fileName

	with open(fileName) as f:
		lines = f.read().splitlines()		
	return lines

def getMaxRadius(center,groupIndex,K):
#get Max radius of the group
	radiusOfPointCloud = []
	
	for i in range(K):
		distances = []
		
		for point in groupIndex[i]:
			c = math.sqrt(((point[0] - center[i][0])** 2) + ((point[1] - center[i][1]) ** 2))
			distances.append(c)
			
		radiusOfPointCloud.append(max(distances))
		
	return radiusOfPointCloud
	
def getAdjMaxtrix(center,radiusOfPointCloud,K):
#get adjencent maxtrix

	adjMatrix = [[0 for _ in range(K)] for _ in range(K)]
	
	for i in range(K):
		for j in range (K):
			center_distance = math.sqrt(((center[i][0] - center[j][0])** 2) + ((center[i][1] - center[j][1]) ** 2))
			if center_distance <= (radiusOfPointCloud[i]+radiusOfPointCloud[j]):
				adjMatrix[i][j] = 1
	return adjMatrix

def getMotifPathInFile(DATA,groupIndex,K,letters):
	path = []
	finalPath = []
	for data in DATA:
		for i in range(K):
			for points in groupIndex[i]:
				if round(points[0],3) == round(data[0],3) and round(points[1],3) == round(data[1],3):
						path.append(letters[i])
	
	for i in range(len(path)):
		if finalPath == []:
			finalPath.append(path[i])
		else:
			if path[i] != finalPath[len(finalPath)-1]:
				finalPath.append(path[i])				
			
			
	return finalPath
					
def findPatternLength(seq):
	result = 1
	if len(seq) %2 != 0:
		max_len = (len(seq) / 2) +1
	else:
		max_len = len(seq) / 2
	for x in range(2, max_len):
		if seq[0:x] == seq[x:2*x] :
			return x
	return result

def findPattern(seq,patternList):
	length = findPatternLength(seq)
	pattern = []
	if len(seq) > 2:
		if length == 1:
			seq = seq[1:]	
			findPattern(seq,patternList)
		else:
			pattern = seq[0:length]
			patternList.append(pattern)
			seq = seq[length*2:]
			findPattern(seq,patternList)


def simplifyPatternList(patternList):
	simplePatternList = []
	[simplePatternList.append(pattern) for pattern in patternList if pattern not in simplePatternList]
	return simplePatternList



def getPatternStringList(simplePatternList):
	patternStringList = []
	for item in simplePatternList:
		patternStr = "".join(item)
		patternStringList.append(patternStr)
	return patternStringList


def getPartition(patternStringList,motionSpace,path):
	partition = [0 for _ in range (len(path))]
	for pat in patternStringList:
		for i in range(len(motionSpace)):
			if pat == motionSpace[i:i+len(pat)]:
				partition[i+len(pat)-1] = 1
	return partition


def getMostFeqLetter(motionSpace,letterList):		
	feq = 0
	letter = ""
	for char in letterList:
		count = motionSpace.count(char)
		if count > feq:
			feq = count
			letter = char
	return letter

def getGroupByPoint(point,center, radiusOfPointCloud,K,letters):
	x = point[0]
	y = point[1]
	group = ""
	for i in range(K):
		distanceBetweenPointAndCenter = math.sqrt(((x - center[i][0])** 2) + ((y - center[i][1]) ** 2))
		if distanceBetweenPointAndCenter <= radiusOfPointCloud[i]:
			group = letters[i]
	return group


def getGraph(adjMatrix,K,letters):
	graph = {}
	for q in range(K):
		graph[letters[q]] = []
	for i in range(len(adjMatrix)):
		line = adjMatrix[i]
		for j in range(len(line)):
			if (i != j) and line[j] == 1:
				graph[letters[i]].append(letters[j])
	return graph
				

class MyQUEUE: # just an implementation of a queue

	def __init__(self):
		self.holder = []

	def enqueue(self,val):
		self.holder.append(val)
	def dequeue(self):
		val = None
		try:
			val = self.holder[0]
			if len(self.holder) == 1:
				self.holder = []
			else:
				self.holder = self.holder[1:]	
		except:
			pass
		return val
	
	def IsEmpty(self):
		result = False
		if len(self.holder) == 0:
			result = True
		return result

def BFS(graph,start,end,q):
	validPath = []
	temp_path = [start]
	q.enqueue(temp_path)
	while q.IsEmpty() == False:
		tmp_path = q.dequeue()
		last_node = tmp_path[len(tmp_path)-1]
		if last_node == end:
			validPath.append("".join(tmp_path))
		for link_node in graph[last_node]:
			if link_node not in tmp_path:
				#new_path = []
				new_path = tmp_path + [link_node]
				q.enqueue(new_path)
	return validPath


def getCommonLetterFromMotif(motif):
	list = []
	for path in motif:
		for char in path:
			if char not in list:
				list.append(char)
	return list

	
def getSubStringList(motifList,mostFeqChar,minMotifLength,maxMotifLength):
	subString = []
	countList = []
	count = 0
	for motif in motifList:
		if mostFeqChar in motif:
			index = motif.index(mostFeqChar)
			for i in range(minMotifLength,maxMotifLength+1):
				for j in range(len(motif) - i + 1):
					if mostFeqChar in motif[j:j+i]:
						subString.append(motif[j:j+i])
						count += 1
		countList.append(count)
		count = 0
					
				
	return subString,countList
	
	
def getSubString(subStringList,subStringCountList):
	list = []
	sum = 0
	for num in subStringCountList:
		list.append(subStringList[sum:num+sum])
		sum += num
	return list

def getMotifWidth(subStrings,groupIndex,K,letters):
	pointsSum = 0
	motifWidth = []
	for subString in subStrings:
		if subString != []:
			subStr = "".join(subString)
			for char in subStr:
				index = letters.index(char)
				pointsSum += len(groupIndex[index])
			avg = pointsSum / len(subString)
			motifWidth.append(avg)
		else:
			motifWidth.append(0)
	
	return motifWidth

def getMotifValueList(subStrings,motifWidth,motifLengthModifier):
	motifValueList = []
	for i in range(len(subStrings)):
		v = (len(subStrings[i]) - 1) * (motifWidth[i]**motifLengthModifier)
		motifValueList.append(v)
		
	return motifValueList
	
	
def getBestMotif(motifValueList,motifList,minMotifLength,maxMotifLength):
	tempMotifList = []
	tempMotifValueList = []
	index = []
	for i in range(len(motifList)):
		if  minMotifLength <= len(motifList[i]) <= maxMotifLength:
			tempMotifList.append(motifList[i])
	
	for motif in tempMotifList:
		index.append(motifList.index(motif))
	
	for idx in index:
		tempMotifValueList.append(motifValueList[idx])
	
	return tempMotifList[tempMotifValueList.index(max(tempMotifValueList))]

def getGroupTokens(K,letters,groupIndex,dataList):
	tokens = [[] for _ in range (5)]
	
	for i in range(5):
		for point in dataList[i]:
			for j in range(K):
				if point in groupIndex[j]:
					if letters[j] not in tokens[i]:
						tokens[i].append(letters[j])
					

	for i in range(5):
		tokens[i] = "".join(tokens[i])
	return tokens

def findPatternFromToken(groupTokens,subStrings,bestMotif,motifList):
	index = motifList.index(bestMotif)
	subStrList = subStrings[index]
	subStrList.reverse()
	existPathInFile = []
	token = []
	for grouptoken in groupTokens:
		for subStr in subStrList:
			if subStr in grouptoken:
				if subStr not in existPathInFile:
					existPathInFile.append(subStr)
					token.append(grouptoken)
					
	return existPathInFile,token
						

			
def getLeftedPath(bestMotif,existPathInFile):
	leftedPath = []
	if existPathInFile == []:
		leftedPath = bestMotif
	else:
		for path in existPathInFile:
			if path in bestMotif:
				motif = ''.join( c for c in bestMotif if  c not in path )
				if motif not in leftedPath:
					leftedPath.append(motif)
	return leftedPath

def findGroupTokenUsingPath(leftedPath,groupTokens):
	tokens = []
	for path in leftedPath:
		for char in path:
			for token in groupTokens:
				if char in token:
					tokens.append(token)
					break
	return tokens
	
def findPointsFromFile(existPathInFile,Tokens,leftedPath,leftedTokens,bestMotif,groupTokens,groupIndex,letters,dataList_old):
	sortedTokens = []
	sortedFiles = []
	sortedgroups = []
	outputData = []
	str = ""
	letter = ""
	current = ""	
	flag = 0
	                
	if existPathInFile == [] and leftedPath == []:
		return []
	for char in bestMotif:
		index_g = letters.index(char)
		sortedgroups.append(index_g)
		if any(char in path for path in existPathInFile):
			sortedTokens.append(Tokens[0])
		else:
			for token in leftedTokens:
				if char in token:	
					sortedTokens.append(token)
					break
					
	rangeOfGroup = [[] for _ in range (len(sortedgroups))]
	nameOfGroup = [0 for _ in range (len(sortedgroups))]
	fileGroupList = [[] for _ in range (len(sortedgroups))]	
	
	for token in sortedTokens:
		idx = groupTokens.index(token)
		file = dataList_old[idx]
		sortedFiles.append(file)

	
	for i in range(len(sortedgroups)):
		for points in sortedFiles[i]:
			if np.float32(points) in groupIndex[sortedgroups[i]]:
				outputData.append(points)
					
		outputData.append("@@")
		
	
	for i in range(len(sortedgroups)):
		for points in sortedFiles[i]:
			for j in range(len(sortedgroups)):
				if np.float32(points) in groupIndex[sortedgroups[j]]:
					str += letters[sortedgroups[j]]
		fileGroupList[i].append(str)
		str = ""
					
	for i in range(len(fileGroupList)):
		letter = letters[sortedgroups[i]]
		for j in range(len(fileGroupList[i][0])):
			
			if letter == fileGroupList[i][0][j] and flag == 0:
				rangeOfGroup[i].append(j*2)
				nameOfGroup[i] = groupTokens.index(sortedTokens[i])
				flag = 1
			if fileGroupList[i][0][j] != letter and flag == 1:
				if j - 1 - rangeOfGroup[i][0] == 0:
					rangeOfGroup[i] = []
					flag = 0
				else:
					rangeOfGroup[i].append((j-1)*2)
					flag = 0
					break
		
	return outputData,rangeOfGroup,nameOfGroup	

def saveToFile(outputData):
	with open('output.txt', 'w') as myfile:
		for output in outputData:
			myfile.write(format(output))
		
def format(data):
	return " ".join((str(data[0]),str(data[1]))) + "\n"

def getFramesForBVH(rangeOfGroup,nameOfGroup,letters):
	frames = []
	firstLastFrames = [[] for _ in range (len(rangeOfGroup))]
	firstFrame = ""
	lastFrame = ""
	data1 = openBVHFile("01_01.bvh")
	data2 = openBVHFile("01_02.bvh")
	data3 = openBVHFile("02_04.bvh")
	data4 = openBVHFile("07_01.bvh")
	data5 = openBVHFile("09_01.bvh")
	skeleton = data1[0:187]
	dataList = [data1[188:],data2[188:],data3[188:],data4[188:],data5[188:]]
	
	for j in range(len(rangeOfGroup)):
		firstFrame = dataList[nameOfGroup[j]][rangeOfGroup[j][0]]
		lastFrame = dataList[nameOfGroup[j]][rangeOfGroup[j][1]]
		firstLastFrames[j].append(firstFrame)
		firstLastFrames[j].append(lastFrame)
		
	length,signs,avgs,bases = CreateTempBVHs(skeleton,firstLastFrames)
	interpolated = getInterpolatedFrames(letters,length,signs,avgs,bases)
	
	for k in range(len(rangeOfGroup)):
		new = dataList[nameOfGroup[k]][rangeOfGroup[k][0]:rangeOfGroup[k][1]]
		if k > (len(rangeOfGroup) - 2):
			frames += new
		else:
			frames += new + interpolated[k]
	
	for q in range(len(frames)):
		tempstr = ""
		templist = frames[q].split()
		templist[0] = "0"
		templist[2] = "0"
		tempstr = " ".join(templist)
		frames[q] = tempstr
	
	skeleton[185] = "Frames: " + str(len(frames))
	file = skeleton + frames
	return file
	
	
def saveBVHToFile(newBVH,fileName):
	with open(fileName, 'w') as myfile:
		myfile.write("\n".join(line for line in newBVH))


def CreateTempBVHs(skeleton,firstLastFrames):
	for i in range(len(firstLastFrames)-1):
		frameOne = ""
		frameTwo = ""
		frames = []
		file = []		
		frameOne = firstLastFrames[i][1]
		frameTwo = firstLastFrames[i+1][0]
		signs,avgs,bases = avgZValues(frameOne,frameTwo)
		frames.append(frameOne)
		frames.append(frameTwo)
		file = skeleton + frames
		saveBVHToFile(file,str(i)+".bvh")
	return (len(firstLastFrames) - 1),signs,avgs,bases
		
def getInterpolatedFrames(letters,length,signs,avgs,bases):
	interpolated = []
	for i in range(length):
		data = openBVHFile(letters[i] + ".bvh")
		frameData = data[188:]	
		frames = fixedFrames(frameData,signs,avgs,bases)
		interpolated.append(frames)
	return interpolated

def avgZValues(frameOne,frameTwo):
	frame1 = frameOne.split()
	frame2 = frameTwo.split()
	
	num1List = []
	num2List = []
	signs = []
	avgs = []
	
	
	for i in range(3):
		num1 = float(frame1[i])
		num2 = float(frame2[i])
		num1List.append(num1)
		num2List.append(num2)
			
	for i in range(3):	
		num1 = num1List[i]
		num2 = num2List[i]
		sign,avg = getFrameSign(num1,num2,checkSign(num1),checkSign(num2))
		signs.append(sign)
		avgs.append(avg)
	return signs,avgs,num1List
	

def getFrameSign(num1,num2,sign1,sign2):
	framenum = 250

	if sign1 == "+" and  sign2 == "-":
		return "-",((num1 - num2) / framenum)

	elif sign1 == "-" and  sign2 == "+":
		return "+",((num2 - num1) / framenum)
	else:
		if num1 > num2:
			return "-",((num1 - num2) / framenum)
		elif num1 == num2:
			return "+",0
		else:
			return "+",((num2 - num1) / framenum)		

def checkSign(num):
	if num > 0:
		return "+"
	elif num == 0:
		return "0"
	else:
		return "-"	
def fixedFrames(frameData,signs,avgs,bases):
	newFrames = []
	for frame in frameData:
		frameList = frame.split()
		for i in range(3):
			if signs[i] == "+":
				frameList[i] = str(bases[i] + avgs[i] )
			else:
				frameList[i] = str(bases[i] - avgs[i] )
		newFrame = " ".join(frameList)
		newFrames.append(newFrame)
	
	return newFrames

main()
