_passmg_completion() {
  local cur
  COMPREPLY=()
  cur="${COMP_WORDS[COMP_CWORD]}"

  COMPREPLY=( $(compgen -W "$(ls $(passmg vault) | grep "^$cur" | sed 's/.json$//')" -- "$cur") )
  return 0
}

complete -F _passmg_completion passmg