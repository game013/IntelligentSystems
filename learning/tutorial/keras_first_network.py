from keras.models import Sequential
from keras.layers import Dense
import numpy
# fix random seed for reproducibility
numpy.random.seed(7)

print 'Loading dataset'
# load pima indians dataset
dataset = numpy.loadtxt("pima-indians-diabetes.csv", delimiter=",")
# split into input (X) and output (Y) variables
X = dataset[:,0:8]
Y = dataset[:,8]
print "X:", X
print "Y:", Y
print 'Dataset loaded'

# create a model
model = Sequential()
model.add(Dense(12, input_dim=8, activation='relu'))
model.add(Dense(8, activation='relu'))
model.add(Dense(1, activation='sigmoid'))
print 'Model created'

# Compile model
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
print 'Model compiled'

# Fit the model
model.fit(X, Y, epochs=150, batch_size=10)
print 'Model trained'

# evaluate the model
scores = model.evaluate(X, Y)
print "Evaluation results:\n%s: %.2f%%" % (model.metrics_names[1], scores[1]*100)

# calculate predictions
predictions = model.predict(X)
# round predictions
rounded = [round(x[0]) for x in predictions]
print 'Predictions:'
print rounded
