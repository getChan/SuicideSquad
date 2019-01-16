import ftplib

filename = "thank(1).mp4"
ftp=ftplib.FTP()
ftp.connect("13.124.162.91", 22)
ftp.login()
# ftp.cwd("받아올  파일 위치")
ftp.retrlines('LIST')
# fd = open("./" + filename,'wb')
# ftp.retrbinary("RETR " + filename, fd.write)
ftp.close()

# from ftplib import FTP
# ftp = FTP('13.124.162.91') # host에 기본 포트로 연결
# ftp.login() # 익명으로 접속(user anonymous, passwd anonymous@)
# # ftp.cwd('debian') # "debian" 디렉토리로 이동
# ftp.retrlines('LIST') # 디렉토리의 내용을 목록화
# # ftp.retrbinary('RETR README', open('README', 'wb').write) # README 파일 저장
# ftp.quit() # FTP 종료