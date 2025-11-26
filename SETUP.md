# 🚀 Quick GitHub Hosting Setup

## What You Have Now

- ✅ `repo.json` - Repository metadata
- ✅ `plugins.json` - Extension listing
- ✅ `builds/TheNkiriProvider.cs3` - Your built extension
- ✅ All source code

## 3-Step Setup

### Step 1: Create GitHub Repository

1. Go to: https://github.com/new
2. Repository name: `thenkiri-cloudstream` (or any name you want)
3. Make it **Public**
4. ✅ Check "Add a README file"
5. Click "Create repository"

### Step 2: Upload Your Files

You can either:

**Option A: Use GitHub Web Interface**

1. Go to your new repository
2. Click "Add file" → "Upload files"
3. Drag and drop the entire `TestPlugins-master` folder contents:
   - `TheNkiriProvider/` folder
   - `builds/` folder
   - `repo.json`
   - `plugins.json`
   - `build.gradle.kts`
   - `settings.gradle.kts`
   - `gradle/` folder
   - `gradlew` and `gradlew.bat`
4. Commit changes

**Option B: Use Git Command Line**

```bash
cd "c:\Users\princ\Desktop\cloud stream extension\TestPlugins-master"

# Initialize git
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - TheNkiri CloudStream Extension"

# Add your GitHub repo as remote
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### Step 3: Update URLs in JSON Files

After uploading, edit these files on GitHub:

**Edit `repo.json`:**
Replace `YOUR_USERNAME` and `YOUR_REPO` with your actual values:
```json
{
  "pluginLists": [
    "https://raw.githubusercontent.com/YOUR_ACTUAL_USERNAME/YOUR_ACTUAL_REPO/main/plugins.json"
  ]
}
```

**Edit `plugins.json`:**
```json
[
  {
    "url": "https://raw.githubusercontent.com/YOUR_ACTUAL_USERNAME/YOUR_ACTUAL_REPO/main/builds/TheNkiriProvider.cs3",
    "repositoryUrl": "https://github.com/YOUR_ACTUAL_USERNAME/YOUR_ACTUAL_REPO"
  }
]
```

## 📱 Add to CloudStream

After setting up GitHub:

1. Open CloudStream app
2. Go to: **Settings** → **Extensions** → **Add Repository**
3. Enter this URL:
   ```
   https://raw.githubusercontent.com/YOUR_USERNAME/YOUR_REPO/main/repo.json
   ```
4. Tap "Add"
5. Your extension will appear in the list
6. Tap to install!

## ✅ Testing

Test your repository URL before sharing:

1. Open in browser:
   ```
   https://raw.githubusercontent.com/YOUR_USERNAME/YOUR_REPO/main/repo.json
   ```
   Should show the JSON content

2. Test the .cs3 file link:
   ```
   https://raw.githubusercontent.com/YOUR_USERNAME/YOUR_REPO/main/builds/TheNkiriProvider.cs3
   ```
   Should download the file

3. Add to CloudStream and install

## 🔄 Updating Your Extension

When you make changes:

1. Build new version: `.\gradlew make` (in `TestPlugins-master` folder)
2. Copy new `.cs3` to `builds/` folder
3. Update version in `TheNkiriProvider/build.gradle.kts`: `version = 2`
4. Update version in `plugins.json`: `"version": 2`
5. Commit and push to GitHub
6. Users will get update notification in CloudStream

## 🎯 Your Final URL

After setup, share this URL:

```
https://raw.githubusercontent.com/YOUR_USERNAME/YOUR_REPO/main/repo.json
```

Users add this once and get all future updates automatically!

## 📝 Example

If your username is `john` and repo is `thenkiri-cloudstream`:

**Repository URL for CloudStream:**
```
https://raw.githubusercontent.com/john/thenkiri-cloudstream/main/repo.json
```

**Direct Extension URL:**
```
https://raw.githubusercontent.com/john/thenkiri-cloudstream/main/builds/TheNkiriProvider.cs3
```

## 🎉 Benefits

✅ One-click install for users  
✅ Automatic updates  
✅ Version control  
✅ Easy to share (just one URL)  
✅ Professional setup  

## 🔧 Optional: Enable GitHub Pages

For a prettier URL:

1. Go to repository Settings
2. Pages → Source → Deploy from branch
3. Select `main` branch
4. Save

Your repo will be at: `https://YOUR_USERNAME.github.io/YOUR_REPO/repo.json`

(Both URLs work, raw.githubusercontent.com is simpler for this use case)

---

**Need help?** Check `HOSTING_GUIDE.md` for detailed instructions!
