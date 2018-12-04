## git 指令
#### git config --global user.name "用户名" 
#### git config --global user.email "邮箱" 
#### ssh-keygen -t rsa -C "邮箱" 
#### git init 创建生成本地仓库
#### git add 文件  添加到暂存区
#### git reset HEAD 文件  取消增加到暂存区的内容
#### git commit -m "描述" 将你添加到暂存区的文件提交到本地仓库
#### git status 检查工作区文件状态
#### git log 查看提交
#### git reset --hard committed 可回退到你提交的文件
#### git branch 查看所有分支
#### git checkout -b 所需创建的分支名   创建并切换到该分支
#### git checkout 要切换的分支名     切换分支
#### git pull   更新本地库至远程库的最新改动
#### git push -u origin master 提交到远程仓库
#### git merge 被合并的分支       合并分支
#### get -version 查看git版本号
#### git remote remove origin 仓库地址 删除远程仓库
#### git remote add origin "远程仓库地址"      关联远程仓库
#### git push -u -f origin  master   第一次向远程仓库推送
#### git push -u origin master -f    强制推送

### 远程分支合并dev分支
  git checkout -b dev  创建出分支并且换到dev分支
  git push origin head -u 将其推到远程仓库  
  git add 
  git commit
  git push origin dev  提交到远程仓库dev
  git pull origin dev  拉取数据
  git checkout master  切换到master
  git merge dev   合并






  
