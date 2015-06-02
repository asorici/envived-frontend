import serial
import requests
import time
import json

ser = serial.Serial('/dev/ttyACM1', 9600, timeout = 1)
j=0
while(True):
    i = 0
    collected = []
    while (i < 15):
        line  = ser.readline()
        if (line[:3] == '***'):
            #print line
            line = line.strip('*')
            tokens = line[:-2].split('|')
            #print tokens
            list_tuple = (float(tokens[0]) , float(tokens[1]), float(tokens[2]), int(tokens[3]), int(tokens[4]), int(tokens[5]) )
            #print list_tuple
            collected.append(list_tuple)
            
            i += 1

    avg_temp = 0
    avg_hum = 0
    avg_lum = 0
    id_t=0
    id_h=0
    id_l=0
    for c in collected:
        avg_temp += c[0]
        avg_hum += c[1]
        avg_lum += c[2]
        id_t = c[3]
        id_h = c[4]
        id_l = c[5]
        
    avg_temp = avg_temp/len(collected)
    avg_hum = avg_hum/len(collected)
    avg_lum = avg_lum/len(collected)

    print "AVT: %.2f AVH: %.2f AVL: %.2f" % (avg_temp, avg_hum, avg_lum)
    id_thing = id_t
    id_thing_h = id_h
    id_thing_l = id_l
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
    
    #daca nu citesc nimic 20 de randuri inchid programul
    count = 0
    while(ser.readline()==''):
        count += 1
        if count == 20:
            break
    
    
    
print "Done"
   
ser.close()
