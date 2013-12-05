git status | tail -n 11 | awk '{print()}' | xargs -I{} git add {}
