#from BeautifulSoup import BeautifulSoup
import urllib2,json
def compare(a,b):#compare
    if a>b:
        return 1
    else:
        return 0
file = open("comedy_comparisons.train", 'r')
a = 0
x = 0
list = []
record = [0,0,0,0]
url_str = 'http://gdata.youtube.com/feeds/api/videos/vdcode?v=2&alt=json'
while True:
    del list[:]
    flag = 0
    line = file.readline()
    #to set the quantity you want to see start from 0
    if not a<20: break
    line = line.rstrip()
    list.append(line.split(","))
    url = url_str.replace("vdcode", list[0][0])
    url2 = url_str.replace("vdcode", list[0][1])
    #verify if valid or not 
    try:
       page = urllib2.urlopen(url)
       page2 = urllib2.urlopen(url2)
    except urllib2.HTTPError, err:
       if err.code == 404:
           #print "Page not found!"
           flag = 1
       elif err.code == 403:
           #print "Access denied!"
           flag = 1
       else:
           #print "Something happened! Error code", err.code
           flag = 1
    except urllib2.URLError, err:
        #print "Some other error happened:", err.reason
        flag = 1
    if flag == 0:
        #print '----------------------'
        #print a+1
        x += 1
        if list[0][2] == 'left':
            data = json.loads(page.read())
            data2 = json.loads(page2.read())
        else:
            data = json.loads(page2.read())
            data2 = json.loads(page.read())
	#retrieve useful information from json
        view1 = int(data['entry']['yt$statistics']['viewCount']) * 1.0
        view2 = int(data2['entry']['yt$statistics']['viewCount']) * 1.0
        favor1 = int(data['entry']['yt$rating']['numLikes']) - int(data['entry']['yt$rating']['numDislikes'])
        favor2 = int(data2['entry']['yt$rating']['numLikes']) - int(data2['entry']['yt$rating']['numDislikes'])
        comment1 = int(data['entry']['gd$comments']['gd$feedLink']['countHint'])
        comment2 = int(data2['entry']['gd$comments']['gd$feedLink']['countHint'])
	#test some possible factors here
        record[0] += compare(view1, view2)
        record[1] += compare(favor1, favor2)
        record[2] += compare(favor1/view1, favor2/view2)
        record[3] += compare(comment1, comment2)
        #print 'label: ', data['entry']['category'][1]['term'], data2['entry']['category'][1]['term']
    #else:
        #print '----------------------'
        #print 'Error', a+1
    a += 1
file.close()
print record
#show total num of valid links
print x
'''
#print soup.prettify()
comment = soup.find('content')
print comment.text
comment = comment.findNext('content')
print comment.text
comment = comment.findNext('content')
print comment.text'''

