# Imports
import serial 
import time
import numpy
import ftplib

# Open serial port
ser = serial.Serial('/my-port',9600)
time.sleep(1)
ser.flush

#Read some data
for i in range(2);
	ser.readline()

#Main loop
while(True):

	#Read data and convert the temperature
	temp = ((int(ser.readline())))