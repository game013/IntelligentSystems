import numpy

def loadDataSet(train=True):
	sufix = 'train' if train else 'eval-java8'
	print 'Loading', sufix, 'dataset'
	# load pima indians dataset
	dataset = numpy.loadtxt("/tmp/code-analyzer/metrics/code-metrics-codeforces-%s.csv" % sufix, delimiter=";")
	# split into input (X) and output (Y) variables
	print 'Train dataset loaded'
	return dataset

def calculateStats(dataset):
	for i in xrange(1, len(dataset[0])):
		Y = dataset[:,i]
		print "%02d %.5f %.5f %.5f %.5f %.5f" % (i, numpy.average(Y), numpy.median(Y), numpy.std(Y), numpy.amin(Y), numpy.amax(Y))

def run():
	dataset = loadDataSet()
	beginners = numpy.array([x for x in dataset if x[0] == 0])
	print 'Stats for beginers:'
	calculateStats(beginners)
	advanced = numpy.array([x for x in dataset if x[0] == 1])
	print 'Stats for advanced:'
	calculateStats(advanced)

run()
