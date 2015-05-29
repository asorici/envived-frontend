import serial
import requests
import time
import json

ser = serial.Serial('/dev/ttyACM1', 9600, timeout = 1)



i = 0
collected = []
while (i < 10):
    line  = ser.readline()
    if (line[:3] == '***'):
        #print line
        line = line.strip('*')
        tokens = line[:-2].split('|')
        #print tokens
        list_tuple = (float(tokens[0]) , float(tokens[1]), float(tokens[2]) )
        #print list_tuple
        collected.append(list_tuple)
        
        i += 1

avg_temp = 0
avg_hum = 0
avg_lum = 0
for c in collected:
    avg_temp += c[0]
    avg_hum += c[1]
    avg_lum += c[2]
    
avg_temp = avg_temp/len(collected)
avg_hum = avg_hum/len(collected)
avg_lum = avg_lum/len(collected)

print "AVT: %.2f AVH: %.2f AVL: %.2f" % (avg_temp, avg_hum, avg_lum)
id_thing = 94
id_thing_h = 93
id_thing_l = 95
v_type = 'temperature'
v_type_h = 'humidity'
v_type_l = 'luminosity'
url = "http://localhost:8080/envived/client/v2/resources/thing_properties/?clientrequest=true&virtual=true&format=json"
payload = {"value" : str(avg_temp), "thing" : "/envived/client/v2/resources/things/" + str(id_thing) + "/", "type" : v_type, "measurement_unit":"celsius"}
payload_h = {"value" : str(avg_hum), "thing" : "/envived/client/v2/resources/things/" + str(id_thing_h) + "/", "type" : v_type_h, "measurement_unit":"percent"}
payload_l = {"value" : str(avg_lum), "thing" : "/envived/client/v2/resources/things/" + str(id_thing_l) + "/", "type" : v_type_l, "measurment_unit":"percent"}
headers = {'content-type': 'application/json'}
r = requests.post(url, data = json.dumps(payload), headers = headers)
print "URL "+url
print "Post status temperature: %s Post reason %s" % (r.status_code, r.reason)
r = requests.post(url, data = json.dumps(payload_h), headers = headers)
print "Post status humidity: %s Post reason %s" % (r.status_code, r.reason)
r = requests.post(url, data = json.dumps(payload_l), headers = headers)
print "Post status luminosity: %s Post reason %s" % (r.status_code, r.reason)



time.sleep(10)
print "Done"
ser.close()
